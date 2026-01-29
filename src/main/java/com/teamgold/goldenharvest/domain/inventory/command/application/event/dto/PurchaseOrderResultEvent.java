package com.teamgold.goldenharvest.domain.inventory.command.application.event.dto;

/*
* 구매 주문의 완료를 알리는 이벤트 객체이다
* 성공 또는 실패를 status로 나타낸다
* status = ("success", "fail")
* */
public record PurchaseOrderResultEvent(
	String purchaseOrderId,
	String status
) {
	public static PurchaseOrderResultEvent create(
		String purchaseOrderId,
		String status
	) {
		return new PurchaseOrderResultEvent(purchaseOrderId, status);
	}
}
