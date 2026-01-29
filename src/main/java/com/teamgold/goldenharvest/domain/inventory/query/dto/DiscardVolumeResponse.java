package com.teamgold.goldenharvest.domain.inventory.query.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DiscardVolumeResponse {
	int currentMonthVolume;
	int lastMonthVolume;
}
