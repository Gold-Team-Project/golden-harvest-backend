package com.teamgold.goldenharvest.domain.sales.command.application.service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.AddToCartRequest;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.CartItemResponse;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.CartResponse;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.RedisCartItem;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.UpdateCartItemRequest;
import com.teamgold.goldenharvest.domain.sales.command.domain.SalesSku;
import com.teamgold.goldenharvest.domain.sales.command.domain.cart.Cart;
import com.teamgold.goldenharvest.domain.sales.command.domain.cart.CartItem;
import com.teamgold.goldenharvest.domain.sales.command.domain.cart.CartStatus;
import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrder;
import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrderItem;
import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrderStatus;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.cart.CartRepository;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.sales_order.SalesOrderRepository;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.sales_order.SalesOrderStatusRepository;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.repository.SalesSkuRepository;
import com.teamgold.goldenharvest.domain.sales.command.infra.InventoryApiClient;
import com.teamgold.goldenharvest.domain.inventory.query.dto.AvailableItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.teamgold.goldenharvest.domain.sales.command.application.event.dto.SalesOrderEvent;
import com.teamgold.goldenharvest.domain.sales.command.application.event.SalesOrderEventPublisher;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors; 

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations; // Object 제네릭으로 변경

    @Mock
    private CartRepository cartRepository;

    @Mock
    private SalesOrderRepository salesOrderRepository;

    @Mock
    private SalesOrderStatusRepository salesOrderStatusRepository;

    @Mock
    private InventoryApiClient inventoryApiClient; // Add this mock

    @Mock
    private SalesOrderEventPublisher eventPublisher;

    private String userEmail;
    private String skuNo;
    private SalesSku salesSku;
    private RedisCartItem redisCartItem;
    private AvailableItemResponse availableItemResponse; // Added

    @BeforeEach
    void setUp() {
        userEmail = "test@example.com";
        skuNo = "SKU001";
        salesSku = SalesSku.builder()
                .skuNo(skuNo)
                .itemName("Test Item")
                .gradeName("A")
                .varietyName("Variety")
                .build();
        redisCartItem = new RedisCartItem(skuNo, "Test Item", "A", "Variety", 1, BigDecimal.valueOf(10000));
        availableItemResponse = AvailableItemResponse.builder() // Added
                .skuNo(skuNo)
                .itemName("Test Item")
                .gradeName("A")
                .varietyName("Variety")
                .customerPrice(10000.0)
                .build();

        // Mock common RedisTemplate behavior
        given(redisTemplate.opsForHash()).willReturn(hashOperations);
    }

    // --- addItemToCart Tests ---
    @Test
    @DisplayName("장바구니에 새 상품 추가 성공")
    void testAddItemToCart_NewItem_Success() {
        // Given
        AddToCartRequest request = new AddToCartRequest(skuNo, 1);
        given(hashOperations.get(anyString(), anyString())).willReturn(null);
        given(inventoryApiClient.findAvailableItemBySkuNo(skuNo)).willReturn(Optional.of(availableItemResponse));

        // When
        cartService.addItemToCart(userEmail, request);

        // Then
        verify(inventoryApiClient, times(1)).findAvailableItemBySkuNo(skuNo);
        verify(hashOperations, times(1)).get(anyString(), eq(skuNo));
        verify(hashOperations, times(1)).put(anyString(), eq(skuNo), any(RedisCartItem.class));
        verify(redisTemplate, times(1)).expire(anyString(), anyLong(), any(TimeUnit.class));
    }

    @Test
    @DisplayName("장바구니에 기존 상품 수량 증가 성공")
    void testAddItemToCart_ExistingItem_QuantityIncreased() {
        // Given
        AddToCartRequest request = new AddToCartRequest(skuNo, 2);
        redisCartItem.setQuantity(1); // Initial quantity
        given(hashOperations.get(anyString(), anyString())).willReturn(redisCartItem);

        // When
        cartService.addItemToCart(userEmail, request);

        // Then
        verify(hashOperations, times(1)).get(anyString(), eq(skuNo));
        // 반환된 RedisCartItem의 수량이 올바르게 업데이트되었는지 검증한다.
        // 실제 객체인 `redisCartItem.addQuantity(request.getQuantity());`에 대해서는 mockito.times(1)로 호출 횟수를 검증할 수 없다.
        assertThat(redisCartItem.getQuantity()).isEqualTo(3); // 1 (initial) + 2 (added)
        verify(hashOperations, times(1)).put(anyString(), eq(skuNo), eq(redisCartItem));
        verify(redisTemplate, times(1)).expire(anyString(), anyLong(), any(TimeUnit.class));
    }

    @Test
    @DisplayName("장바구니에 상품 추가 실패 - 상품을 찾을 수 없음")
    void testAddItemToCart_ProductNotFound_ThrowsException() {
        // Given
        AddToCartRequest request = new AddToCartRequest(skuNo, 1);
        given(inventoryApiClient.findAvailableItemBySkuNo(skuNo)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> cartService.addItemToCart(userEmail, request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PRODUCT_NOT_FOUND);

        verify(inventoryApiClient, times(1)).findAvailableItemBySkuNo(skuNo);
        verify(hashOperations, times(1)).get(anyString(), anyString()); // 상품을 찾을 수 없어도 장바구니 확인은 먼저 한다.
        verify(hashOperations, never()).put(anyString(), anyString(), any(RedisCartItem.class));
    }

    // --- getCart Tests ---
    @Test
    @DisplayName("장바구니 조회 성공 - 상품 존재")
            void testGetCart_Success_WithItems() {
                // Given
                Map<Object, Object> redisCartItems = new HashMap<>(); // Object, Object로 변경함
        redisCartItems.put(skuNo, redisCartItem);
        given(hashOperations.entries(anyString())).willReturn(redisCartItems);

        // When
        CartResponse response = cartService.getCart(userEmail);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getUserEmail()).isEqualTo(userEmail);
        assertThat(response.getItems()).hasSize(1); // getItems()로 변경함
        assertThat(response.getItems().get(0).getSkuNo()).isEqualTo(skuNo); // getItems()로 변경함
        verify(redisTemplate, times(1)).expire(anyString(), anyLong(), any(TimeUnit.class));
    }

    @Test
    @DisplayName("장바구니 조회 성공 - 장바구니 비어있음")
    void testGetCart_Success_EmptyCart() {
        // Given
        given(hashOperations.entries(anyString())).willReturn(Collections.emptyMap());

        // When
        CartResponse response = cartService.getCart(userEmail);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getUserEmail()).isEqualTo(userEmail);
        assertThat(response.getItems()).isEmpty(); // getItems()로 변경함
        verify(redisTemplate, times(0)).expire(anyString(), anyLong(), any(TimeUnit.class)); // No need to expire empty cart
    }

    // --- updateItemQuantity Tests ---
    @Test
    @DisplayName("장바구니 상품 수량 업데이트 성공")
    void testUpdateItemQuantity_Success() {
        // Given
        UpdateCartItemRequest request = new UpdateCartItemRequest(skuNo, 5);
        given(hashOperations.get(anyString(), anyString())).willReturn(redisCartItem);

        // When
        cartService.updateItemQuantity(userEmail, request);

        // Then
        verify(hashOperations, times(1)).get(anyString(), eq(skuNo));
        assertThat(redisCartItem.getQuantity()).isEqualTo(5); // 실제 수량 변경이 발생했는지 검증한다.
        verify(hashOperations, times(1)).put(anyString(), eq(skuNo), eq(redisCartItem));
        verify(redisTemplate, times(1)).expire(anyString(), anyLong(), any(TimeUnit.class));
    }

    @Test
    @DisplayName("장바구니 상품 수량 업데이트 실패 - 상품을 찾을 수 없음")
    void testUpdateItemQuantity_ItemNotFound_ThrowsException() {
        // Given
        UpdateCartItemRequest request = new UpdateCartItemRequest(skuNo, 5);
        given(hashOperations.get(anyString(), anyString())).willReturn(null);

        // When & Then
        assertThatThrownBy(() -> cartService.updateItemQuantity(userEmail, request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.RESOURCE_NOT_FOUND);

        verify(hashOperations, times(1)).get(anyString(), eq(skuNo));
        verify(hashOperations, never()).put(anyString(), anyString(), any(RedisCartItem.class));
    }

    @Test
    @DisplayName("장바구니 상품 수량 0 이하로 업데이트 시 삭제 성공")
    void testUpdateItemQuantity_ZeroOrLess_RemovesItem() {
        // Given
        UpdateCartItemRequest request = new UpdateCartItemRequest(skuNo, 0);
        given(hashOperations.get(anyString(), anyString())).willReturn(redisCartItem);

        // When
        cartService.updateItemQuantity(userEmail, request);

        // Then
        verify(hashOperations, times(1)).get(anyString(), eq(skuNo));
        verify(hashOperations, times(1)).delete(anyString(), eq(skuNo)); // 해당 항목은 삭제되어야 한다
        verify(hashOperations, never()).put(anyString(), anyString(), any(RedisCartItem.class));
        verify(redisTemplate, times(1)).expire(anyString(), anyLong(), any(TimeUnit.class));
    }

    // --- removeItem Tests ---
    @Test
    @DisplayName("장바구니 상품 제거 성공")
    void testRemoveItem_Success() {
        // Given
        // hashOperations.delete에 대해서는 기본적인 mock 동작 외에 별도의 설정이 필요하지 않다.

        // When
        cartService.removeItem(userEmail, skuNo);

        // Then
        verify(hashOperations, times(1)).delete(anyString(), eq(skuNo));
        verify(redisTemplate, times(1)).expire(anyString(), anyLong(), any(TimeUnit.class));
    }

    // --- placeOrder Tests ---
    @Test
    @DisplayName("주문 생성 성공 - 장바구니 상품 존재")
    void testPlaceOrder_Success_WithItems() {
        // Given
        Map<Object, Object> redisCartItems = new HashMap<>(); // Object, Object로 변경함
        redisCartItems.put(skuNo, redisCartItem);
        String skuNo2 = "SKU002";
        RedisCartItem redisCartItem2 = new RedisCartItem(skuNo2, "Test Item 2", "B", "Variety", 2, BigDecimal.valueOf(5000));
        redisCartItems.put(skuNo2, redisCartItem2);

        given(hashOperations.entries(anyString())).willReturn(redisCartItems);

        SalesOrderStatus pendingStatus = new SalesOrderStatus(1L, "주문 접수", "PENDING");
        given(salesOrderStatusRepository.findBySalesStatusType("PENDING")).willReturn(Optional.of(pendingStatus));
        given(cartRepository.save(any(Cart.class))).willAnswer(invocation -> invocation.getArgument(0));
        given(salesOrderRepository.save(any(SalesOrder.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        String resultOrderId = cartService.placeOrder(userEmail);

        // Then
        assertThat(resultOrderId).isNotNull();
        verify(hashOperations, times(1)).entries(anyString());
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(salesOrderStatusRepository, times(1)).findBySalesStatusType("PENDING");
        verify(salesOrderRepository, times(1)).save(any(SalesOrder.class));
        verify(redisTemplate, times(1)).delete(anyString());
        // Then: 주문 생성 이벤트 발행 검증
        verify(eventPublisher, times(2)).publishSalesOrderEvent(any(SalesOrderEvent.class));
    }

    @Test
    @DisplayName("주문 생성 실패 - 장바구니 비어있음")
    void testPlaceOrder_EmptyCart_ThrowsException() {
        // Given
        given(hashOperations.entries(anyString())).willReturn(Collections.emptyMap());

        // When & Then
        assertThatThrownBy(() -> cartService.placeOrder(userEmail))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CART_EMPTY);

        verify(hashOperations, times(1)).entries(anyString());
        verify(cartRepository, never()).save(any(Cart.class));
        verify(salesOrderStatusRepository, never()).findBySalesStatusType(anyString());
        verify(salesOrderRepository, never()).save(any(SalesOrder.class));
        verify(redisTemplate, never()).delete(anyString());
    }

    @Test
    @DisplayName("주문 생성 실패 - PENDING 상태를 찾을 수 없음")
    void testPlaceOrder_PendingStatusNotFound_ThrowsException() {
        // Given
        Map<Object, Object> redisCartItems = new HashMap<>(); // Object, Object로 변경함
        redisCartItems.put(skuNo, redisCartItem);
        given(hashOperations.entries(anyString())).willReturn(redisCartItems);
        given(salesOrderStatusRepository.findBySalesStatusType("PENDING")).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> cartService.placeOrder(userEmail))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.RESOURCE_NOT_FOUND);

        verify(hashOperations, times(1)).entries(anyString());
        verify(cartRepository, times(1)).save(any(Cart.class)); // never()에서 times(1)로 변경함
        verify(salesOrderStatusRepository, times(1)).findBySalesStatusType("PENDING"); // 한 번 호출되었는지 검증한다
        verify(salesOrderRepository, never()).save(any(SalesOrder.class));
        verify(redisTemplate, never()).delete(anyString());
    }
}
