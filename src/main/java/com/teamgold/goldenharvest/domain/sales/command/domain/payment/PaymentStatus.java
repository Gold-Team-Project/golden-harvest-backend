package com.teamgold.goldenharvest.domain.sales.command.domain.payment;

public enum PaymentStatus {
    READY, // 결제 대기
    PAID, // 결제 완료
    FAILED, // 결제 실패
    CANCELED // 결제 취소
}
