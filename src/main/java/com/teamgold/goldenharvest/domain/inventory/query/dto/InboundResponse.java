package com.teamgold.goldenharvest.domain.inventory.query.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InboundResponse {
	String inboundId;
	String purchaseOrderId;
	String skuNo;
	Integer quantity;
	LocalDate inboundDate;
}
