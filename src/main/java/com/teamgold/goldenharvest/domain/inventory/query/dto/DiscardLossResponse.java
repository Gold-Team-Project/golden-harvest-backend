package com.teamgold.goldenharvest.domain.inventory.query.dto;

import lombok.Builder;

@Builder
public class DiscardLossResponse {
	Double currentTotalValue;
	Double lastMonthTotalValue;
}
