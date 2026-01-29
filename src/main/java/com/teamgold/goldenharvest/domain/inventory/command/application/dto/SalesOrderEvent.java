package com.teamgold.goldenharvest.domain.inventory.command.application.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SalesOrderEvent(
	String salesOrderItemId,
	String skuNo,
	BigDecimal salesPrice,
	Integer quantity
) { }
