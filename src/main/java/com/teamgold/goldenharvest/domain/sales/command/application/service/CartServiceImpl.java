package com.teamgold.goldenharvest.domain.sales.command.application.service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.AddToCartRequest;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.RedisCartItem;
import com.teamgold.goldenharvest.domain.sales.command.domain.SalesSku;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.repository.SalesSkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

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
            BigDecimal unitPrice = BigDecimal.valueOf(10000);
            RedisCartItem newItem = RedisCartItem.from(salesSku, request.getQuantity(), unitPrice);
            hashOperations.put(cartKey, request.getSkuNo(), newItem);
        }

        // 4. 장바구니 만료 시간 설정
        redisTemplate.expire(cartKey, CART_EXPIRE_DAYS, TimeUnit.DAYS);
    }
}
