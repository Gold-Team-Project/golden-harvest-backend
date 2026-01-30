package com.teamgold.goldenharvest.domain.inventory.query.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class DiscardResponse {
	String discardId;
	String lotNo;
	Integer quantity;
	LocalDateTime discardedAt;
	String approvedEmailId;
	String discardStatus;
	Double discardRate;
	String itemName;
}
