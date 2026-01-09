package com.teamgold.goldenharvest.domain.sales.command.domain.payment;

public enum PaymentEventType {
    REQUEST, //결제 요청
    APPROVED, // 결제 승인
    FAILED, // 결제 실패
    CANCELED, // 결제 취소
    WEBHOOK // 웹훅 수신
}
