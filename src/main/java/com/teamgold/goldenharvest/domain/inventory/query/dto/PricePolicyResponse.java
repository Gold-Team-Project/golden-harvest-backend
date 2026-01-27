package com.teamgold.goldenharvest.domain.inventory.query.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public class PricePolicyResponse {
	String skuNo;
	BigDecimal marginRate;
	Boolean isActive;
}
