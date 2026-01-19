package com.teamgold.goldenharvest.domain.inventory.command.application.dto;

import java.math.BigDecimal;

public record SalesOrderEvent(
	String salesOrderItemId,
	String skuNo,
	BigDecimal salesPrice,
	Integer quantity
) {
}
