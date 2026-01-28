package com.teamgold.goldenharvest.domain.inventory.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AvailableItemResponse {
	String skuNo;
	Integer quantity;
	String itemName;
	String gradeName;
	String varietyName;
	String baseUnit;
	Double customerPrice;
	String fileUrl;
}
