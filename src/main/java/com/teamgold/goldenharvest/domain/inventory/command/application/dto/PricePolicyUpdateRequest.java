package com.teamgold.goldenharvest.domain.inventory.command.application.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PricePolicyUpdateRequest {
	String skuNo;

	@NotBlank
	@Min(0) @Max(1)
	BigDecimal marginRate;

	Boolean activeStatus;
}
