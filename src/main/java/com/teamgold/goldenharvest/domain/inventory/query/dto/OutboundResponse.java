package com.teamgold.goldenharvest.domain.inventory.query.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OutboundResponse {
	String outboundId;
	String salesOrderItemId;
	String lotNo;
	String skuNo;
	LocalDate outboundDate;
	Integer quantity;
	BigDecimal outboundPrice;
}
