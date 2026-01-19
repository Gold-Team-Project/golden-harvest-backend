package com.teamgold.goldenharvest.domain.sales.query.application.dto.response;


import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderResponse {
    private String salesOrderId; // 주문 번호 pk
    private String orderStatus; // 주문 상태
    private LocalDate createdAt; // 주문 일자
    private BigDecimal totalAmount; // 총 결제 금액
    private List<MyOrderItemResponse> items; // 주문 상품 목록

    public static MyOrderResponse from(SalesOrder salesOrder) {
        return MyOrderResponse.builder()
                .salesOrderId(salesOrder.getSalesOrderId())
                .orderStatus(salesOrder.getOrderStatus().getSalesStatusName())
                .createdAt(salesOrder.getCreatedAt())
                .totalAmount(salesOrder.getTotalAmount())
                .items(salesOrder.getSalesOrderItems().stream()
                        .map(MyOrderItemResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
