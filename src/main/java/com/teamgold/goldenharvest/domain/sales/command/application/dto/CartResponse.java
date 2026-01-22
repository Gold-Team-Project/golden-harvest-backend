package com.teamgold.goldenharvest.domain.sales.command.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class CartResponse {
    private String userEmail;
    private List<CartItemResponse> items;
    private BigDecimal totalCartPrice; // 장바구니 전체 금액

    public static CartResponse from(String userEmail, List<CartItemResponse> items) {
        BigDecimal totalCartPrice = items.stream()
                .map(CartItemResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .userEmail(userEmail)
                .items(items)
                .totalCartPrice(totalCartPrice)
                .build();
    }
}
