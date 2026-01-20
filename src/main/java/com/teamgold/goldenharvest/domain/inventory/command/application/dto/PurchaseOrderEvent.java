package com.teamgold.goldenharvest.domain.inventory.command.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/*
* 구매 주문 처리의 완료 이후 입고 및 재고 등록을 위해
* Inventory 도메인으로 전달될 이벤트 데이터 DTO이다
* */
public record PurchaseOrderEvent(
	@NotBlank
	@Pattern(regexp = "^po.*", message = "ID는 'po'로 시작해야 합니다.")
	String purchaseOrderId,

	@NotBlank
	@Pattern(regexp = "^sku.*", message = "SKU는 'sku'로 시작해야 합니다.")
	String skuNo,

	@Min(value = 1, message = "수량은 1 이상이어야 합니다.")
	Integer quantity
) { }
