package com.teamgold.goldenharvest.domain.inventory.command.application.dto;

public record SalesOrderResultEvent(
	String salesOrderItemId,
	String status
) {
	public static SalesOrderResultEvent create(
		String salesOrderItemId,
		String status
	) {
		return new SalesOrderResultEvent(salesOrderItemId, status);
	}
}