package com.teamgold.goldenharvest.domain.inventory.query.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DiscardLossResponse {
	Double currentTotalValue;
	Double lastMonthTotalValue;
}
