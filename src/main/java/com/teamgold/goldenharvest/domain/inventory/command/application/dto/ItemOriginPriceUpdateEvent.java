package com.teamgold.goldenharvest.domain.inventory.command.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ItemOriginPriceUpdateEvent(
	String skuNo,
	LocalDate updatedDate,
	BigDecimal originPrice
) { }
