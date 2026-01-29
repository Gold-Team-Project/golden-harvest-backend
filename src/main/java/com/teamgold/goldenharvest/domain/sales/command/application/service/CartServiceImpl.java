package com.teamgold.goldenharvest.domain.sales.command.application.service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.inventory.query.dto.AvailableItemResponse;
import com.teamgold.goldenharvest.domain.sales.command.application.event.dto.SalesOrderEvent;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.AddToCartRequest;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.CartItemResponse;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.CartResponse;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.RedisCartItem;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.UpdateCartItemRequest;
import com.teamgold.goldenharvest.domain.sales.command.application.event.SalesOrderEventPublisher;
import com.teamgold.goldenharvest.domain.sales.command.domain.SalesSku;
import com.teamgold.goldenharvest.domain.sales.command.domain.cart.Cart;
import com.teamgold.goldenharvest.domain.sales.command.domain.cart.CartItem;
import com.teamgold.goldenharvest.domain.sales.command.domain.cart.CartStatus;
import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrder;
import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrderItem;
import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrderStatus;
import com.teamgold.goldenharvest.domain.sales.command.infra.InventoryApiClient;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.cart.CartRepository;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.sales_order.SalesOrderRepository;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.sales_order.SalesOrderStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final InventoryApiClient inventoryApiClient;
    private final CartRepository cartRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderStatusRepository salesOrderStatusRepository;
    private final SalesOrderEventPublisher eventPublisher;
    private static final String CART_PREFIX = "cart:";
    private static final long CART_EXPIRE_DAYS = 30;

    @Override
    public void addItemToCart(String authorizationHeader, String userEmail, AddToCartRequest request) {
        String cartKey = CART_PREFIX + userEmail;
        HashOperations<String, String, RedisCartItem> hashOperations = redisTemplate.opsForHash();

        // 1. Redis에서 장바구니에 이미 상품이 있는지 확인
        RedisCartItem existingItem = hashOperations.get(cartKey, request.getSkuNo());

        if (existingItem != null) {
            // 2a. 상품이 이미 있으면 수량만 추가
            existingItem.addQuantity(request.getQuantity());
            hashOperations.put(cartKey, request.getSkuNo(), existingItem);
        } else {
            // 2b. 상품이 없으면 Inventory 서비스를 통해 상품 정보 조회
            AvailableItemResponse item = inventoryApiClient.findAvailableItemBySkuNo(authorizationHeader, request.getSkuNo())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

            // 3. 상품 정보를 바탕으로 장바구니 아이템 생성
            RedisCartItem newItem = RedisCartItem.from(item, request.getQuantity());
            hashOperations.put(cartKey, request.getSkuNo(), newItem);
        }

        // 4. 장바구니 만료 시간 설정 (추가/수정 시 갱신)
        redisTemplate.expire(cartKey, CART_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override
    public CartResponse getCart(String userEmail) {
        String cartKey = CART_PREFIX + userEmail;
        HashOperations<String, String, RedisCartItem> hashOperations = redisTemplate.opsForHash();

        Map<String, RedisCartItem> redisCartItems = hashOperations.entries(cartKey);

        if (redisCartItems.isEmpty()) {
            return CartResponse.from(userEmail, Collections.emptyList());
        }

        List<CartItemResponse> cartItemResponses = redisCartItems.values().stream()
                .map(CartItemResponse::fromRedisCartItem)
                .collect(Collectors.toList());

        // 장바구니 조회 시에도 만료 시간 갱신 (사용자가 장바구니를 보고 있다면 활성 상태로 유지)
        redisTemplate.expire(cartKey, CART_EXPIRE_DAYS, TimeUnit.DAYS);

        return CartResponse.from(userEmail, cartItemResponses);
    }

    @Override
    public void updateItemQuantity(String userEmail, UpdateCartItemRequest request) {
        String cartKey = CART_PREFIX + userEmail;
        HashOperations<String, String, RedisCartItem> hashOperations = redisTemplate.opsForHash();

        RedisCartItem existingItem = hashOperations.get(cartKey, request.getSkuNo());

        if (existingItem == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        if (request.getQuantity() <= 0) {
            // 수량이 0 이하면 장바구니에서 해당 상품을 삭제 (선택사항: 에러 처리 또는 최소 수량 1로 조정)
            hashOperations.delete(cartKey, request.getSkuNo());
        } else {
            existingItem.setQuantity(request.getQuantity());
            hashOperations.put(cartKey, request.getSkuNo(), existingItem);
        }

        // 장바구니 만료 시간 갱신
        redisTemplate.expire(cartKey, CART_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override
    public void removeItem(String userEmail, String skuNo) {
        String cartKey = CART_PREFIX + userEmail;
        HashOperations<String, String, RedisCartItem> hashOperations = redisTemplate.opsForHash();

        hashOperations.delete(cartKey, skuNo);

        // 장바구니 만료 시간 갱신
        redisTemplate.expire(cartKey, CART_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override
    @Transactional
    public String placeOrder(String userEmail) {
        String cartKey = CART_PREFIX + userEmail;
        HashOperations<String, String, RedisCartItem> hashOperations = redisTemplate.opsForHash();

        Map<String, RedisCartItem> redisCartItems = hashOperations.entries(cartKey);

        if (redisCartItems.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        // ----------------------------------------------------
        // 1. tb_cart에 장바구니 정보 주문 완료 상태로 저장
        // ----------------------------------------------------
        String cartId = UUID.randomUUID().toString();
        Cart cart = Cart.builder()
                .cartId(cartId)
                .userEmail(userEmail)
                .status(CartStatus.ORDERED)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

        List<CartItem> cartItems = redisCartItems.values().stream()
                .map(redisCartItem -> CartItem.builder()
                        .cartItemId(UUID.randomUUID().toString())
                        .cart(cart)
                        .skuNo(redisCartItem.getSkuNo())
                        .quantity(redisCartItem.getQuantity())
                        .unitPrice(redisCartItem.getUnitPrice())
                        .build())
                .collect(Collectors.toList());
        cart.setCartItems(cartItems);
        cartRepository.save(cart);

        // ----------------------------------------------------
        // 2. tb_sales_order와 tb_sales_order_item에 주문 정보 저장
        // ----------------------------------------------------
        String salesOrderId = UUID.randomUUID().toString();
        SalesOrderStatus defaultOrderStatus = salesOrderStatusRepository.findBySalesStatusType("PENDING")
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        BigDecimal totalAmount = redisCartItems.values().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        SalesOrder salesOrder = SalesOrder.builder()
                .salesOrderId(salesOrderId)
                .userEmail(userEmail)
                .orderStatus(defaultOrderStatus)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .deliveryDate(LocalDate.now().plusDays(3)) // 임시 배송일 3일 뒤
                .totalAmount(totalAmount)
                .build();

        List<SalesOrderItem> salesOrderItems = redisCartItems.values().stream()
                .map(redisCartItem -> SalesOrderItem.builder()
                        .salesOrderItemId(UUID.randomUUID().toString())
                        .salesOrder(salesOrder)
                        .skuNo(redisCartItem.getSkuNo())
                        .quantity(redisCartItem.getQuantity())
                        .price(redisCartItem.getUnitPrice())
                        .build())
                .collect(Collectors.toList());
        salesOrder.setSalesOrderItems(salesOrderItems); // SalesOrder 엔티티에 SalesOrderItem 리스트 설정
        salesOrderRepository.save(salesOrder);

        // ----------------------------------------------------
        // 3. Redis 장바구니 데이터 삭제
        // ----------------------------------------------------
        redisTemplate.delete(cartKey);

        // 주문 생성 이벤트 발행
        for (SalesOrderItem item : salesOrderItems) {
            eventPublisher.publishSalesOrderEvent(
                    SalesOrderEvent.builder()
                            .salesOrderItemId(item.getSalesOrderItemId())
                            .salesPrice(item.getPrice())
                            .skuNo(item.getSkuNo())
                            .quantity(item.getQuantity())
                            .build()
            );
        }

        return salesOrderId; // 새로 생성된 SalesOrder ID 반환
    }
}
