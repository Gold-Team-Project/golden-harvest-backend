package com.teamgold.goldenharvest.domain.sales.command.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter // Redis에서 역직렬화 시 필요
@Builder
public class CartItemResponse {
    private String skuNo;
    private String itemName;
    private String gradeName;
    private String varietyName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice; // (quantity * unitPrice)

    public static CartItemResponse fromRedisCartItem(RedisCartItem redisCartItem) {
        BigDecimal totalPrice = redisCartItem.getUnitPrice().multiply(BigDecimal.valueOf(redisCartItem.getQuantity()));
        return CartItemResponse.builder()
                .skuNo(redisCartItem.getSkuNo())
                .itemName(redisCartItem.getItemName())
                .gradeName(redisCartItem.getGradeName())
                .varietyName(redisCartItem.getVarietyName())
                .quantity(redisCartItem.getQuantity())
                .unitPrice(redisCartItem.getUnitPrice())
                .totalPrice(totalPrice)
                .build();
    }
}
