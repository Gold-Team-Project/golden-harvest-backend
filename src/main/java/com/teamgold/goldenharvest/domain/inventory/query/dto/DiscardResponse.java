package com.teamgold.goldenharvest.domain.inventory.query.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DiscardResponse {
	String discardId;
	String lotNo;
	Integer quantity;
	LocalDateTime discardedAt;
	String approvedEmailId;
	String discardStatus;
	BigDecimal discardRate;
}
