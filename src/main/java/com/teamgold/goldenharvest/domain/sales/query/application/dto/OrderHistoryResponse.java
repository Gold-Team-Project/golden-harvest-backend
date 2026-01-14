package com.teamgold.goldenharvest.domain.sales.query.application.dto;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class OrderHistoryResponse {
    private String salesOrderId;
    private String orderStatus;
    private LocalDate createdAt;
    private BigDecimal totalAmount;
    private List<OrderHistoryItem> orderItems;
}
