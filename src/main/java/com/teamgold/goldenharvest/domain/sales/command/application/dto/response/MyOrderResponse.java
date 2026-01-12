package com.teamgold.goldenharvest.domain.sales.command.application.dto.response;


import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrder;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class MyOrderResponse {
    private String salesOrderId; // 주문 번호 pk
    private String orderStatus; // 주문 상태
    private LocalDate createdAt; // 주문 일자
    private BigDecimal totalAmount; // 총 결제 금액

    public static MyOrderResponse from(SalesOrder salesOrder) {
        return MyOrderResponse.builder()
                .salesOrderId(salesOrder.getSalesOrderId())
                .orderStatus(salesOrder.getOrderStatus().getSalesStatusName()) // 예시: SalesOrderStatus에 getStatus()가 있다고 가정
                .createdAt(salesOrder.getCreatedAt())
                .totalAmount(salesOrder.getTotalAmount())
                .build();
    }
}
