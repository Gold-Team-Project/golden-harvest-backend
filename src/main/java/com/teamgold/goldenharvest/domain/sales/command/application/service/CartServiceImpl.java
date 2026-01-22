package com.teamgold.goldenharvest.domain.sales.command.application.service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.AddToCartRequest;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.CartItemResponse;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.CartResponse;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.RedisCartItem;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.UpdateCartItemRequest;
import com.teamgold.goldenharvest.domain.sales.command.domain.SalesSku;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.repository.SalesSkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SalesSkuRepository salesSkuRepository;
    private static final String CART_PREFIX = "cart:";
    private static final long CART_EXPIRE_DAYS = 30;

    @Override
    public void addItemToCart(String userEmail, AddToCartRequest request) {
        String cartKey = CART_PREFIX + userEmail;
        HashOperations<String, String, RedisCartItem> hashOperations = redisTemplate.opsForHash();

        // 1. DB에서 상품 정보 조회
        SalesSku salesSku = salesSkuRepository.findById(request.getSkuNo())
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        // 2. Redis에서 장바구니에 이미 상품이 있는지 확인
        RedisCartItem existingItem = hashOperations.get(cartKey, request.getSkuNo());

        if (existingItem != null) {
            // 3a. 상품이 이미 있으면 수량만 추가
            existingItem.addQuantity(request.getQuantity());
            hashOperations.put(cartKey, request.getSkuNo(), existingItem);
        } else {
            // 3b. 상품이 없으면 새로 생성
            // TODO: 실제 가격 조회 로직으로 변경 필요
            BigDecimal unitPrice = BigDecimal.valueOf(10000); // 임시 가격
            RedisCartItem newItem = RedisCartItem.from(salesSku, request.getQuantity(), unitPrice);
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
}
