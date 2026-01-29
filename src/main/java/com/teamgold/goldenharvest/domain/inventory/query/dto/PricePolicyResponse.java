package com.teamgold.goldenharvest.domain.inventory.query.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PricePolicyResponse {
	String skuNo;
	BigDecimal marginRate;
	Boolean isActive;
}
