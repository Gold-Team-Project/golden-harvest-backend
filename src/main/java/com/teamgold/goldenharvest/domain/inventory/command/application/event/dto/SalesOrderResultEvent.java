package com.teamgold.goldenharvest.domain.inventory.command.application.event.dto;

import lombok.Builder;

@Builder
public record SalesOrderResultEvent(
	String salesOrderItemId,
	String status
) { }