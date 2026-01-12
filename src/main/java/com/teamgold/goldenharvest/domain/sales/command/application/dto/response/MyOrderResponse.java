package com.teamgold.goldenharvest.domain.sales.command.application.dto.response;


import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrder;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class MyOrderResponse {
    private String salesOrderId;
    private String orderStatus;
    private LocalDate createdAt;
    private BigDecimal totalAmount;

    public static MyOrderResponse from(SalesOrder salesOrder) {
        return MyOrderResponse.builder()
                .salesOrderId(salesOrder.getSalesOrderId())
                .orderStatus(salesOrder.getOrderStatus().getSalesStatusName()) // 예시: SalesOrderStatus에 getStatus()가 있다고 가정
                .createdAt(salesOrder.getCreatedAt())
                .totalAmount(salesOrder.getTotalAmount())
                .build();
    }
}
