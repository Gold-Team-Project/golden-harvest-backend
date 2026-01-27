package com.teamgold.goldenharvest.domain.sales.query.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderDetailResponse {
    private String salesOrderId; // 주문번호
    private String orderStatus; // 상태
    private LocalDate createdAt; // 생성일
    private BigDecimal totalAmount; // 총 가격
    private String company; // 회사 이름

    private String name; // 고객 이름
    private String phoneNumber; // 고객 전화번호
    private String addressLine1; // 주소1
    private String addressLine2; // 주소2
    private String postalCode; // 우편번호

    private List<OrderHistoryItem> orderItems;
}
