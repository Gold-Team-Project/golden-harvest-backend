package com.teamgold.goldenharvest.domain.sales.query.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryItem {
    private String itemName; // 품목명
    private String gradeName; // 등급명
    private String varietyName; // 상품명
    private Integer quantity; // 수량
    private BigDecimal price; //  가격
}
