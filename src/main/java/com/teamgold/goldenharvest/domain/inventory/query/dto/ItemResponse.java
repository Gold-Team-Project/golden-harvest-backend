package com.teamgold.goldenharvest.domain.inventory.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ItemResponse {
	String lotNo;
	String skuNo;
	Integer quantity;
	String itemName;
	String gradeName;
	String varietyName;
	String baseUnit;
	String status;
	LocalDate inboundDate;
}
