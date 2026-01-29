package com.teamgold.goldenharvest.domain.sales.command.application.event.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SalesOrderEvent(
	String salesOrderItemId,
	String skuNo,
	BigDecimal salesPrice,
	Integer quantity
) { }
