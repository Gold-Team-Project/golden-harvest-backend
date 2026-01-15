package com.teamgold.goldenharvest.domain.inventory.command.application.dto;

public record ItemMasterUpdatedData(
	String skuNo,
	String itemName,
	String gradeName,
	String varietyName,
	String baseUnit,
	Boolean isActive
) { }
