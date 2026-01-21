package com.teamgold.goldenharvest.domain.inventory.command.application.dto;

import lombok.Getter;

@Getter
public class DiscardItemRequest {
	private String lotNo;
	private Integer quantity;
	private String discardStatus;
	private String description;
	private String approvedAdminEmail;
}
