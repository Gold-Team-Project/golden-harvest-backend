package com.teamgold.goldenharvest.domain.inventory.command.application.dto;

/*
* 구매 주문 처리의 완료 이후 입고 및 재고 등록을 위해
* Inventory 도메인으로 전달될 이벤트 데이터 DTO이다
* */
public record PurchaseOrderEvent(
	String purchaseOrderId,
	String skuNo,
	Integer quantity
) { }
