package com.teamgold.goldenharvest.domain.inventory.query.dto;

import lombok.Builder;

@Builder
public class DiscardVolumeResponse {
	int currentMonthVolume;
	int lastMonthVolume;
}
