package com.teamgold.goldenharvest.domain.inventory.command.application.dto;

import lombok.Getter;

@Getter
public class DiscardItemRequest {
	String lotNo;
	Integer quantity;
	String discardStatus;
	String description;
}
