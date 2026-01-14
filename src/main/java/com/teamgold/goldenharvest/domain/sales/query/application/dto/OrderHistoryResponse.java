package com.teamgold.goldenharvest.domain.sales.query.application.dto;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class OrderHistoryResponse {
    private String salesOrderId; // 주문번호
    private String orderStatus; // 상태
    private LocalDate createdAt; // 생성일
    private BigDecimal totalAmount; // 총 가격
    private List<OrderHistoryItem> orderItems;
}
