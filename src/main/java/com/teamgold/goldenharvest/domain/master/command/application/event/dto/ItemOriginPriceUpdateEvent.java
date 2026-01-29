package com.teamgold.goldenharvest.domain.master.command.application.event.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ItemOriginPriceUpdateEvent(
	String skuNo,
	LocalDate updatedDate,
	BigDecimal originPrice
) { }
