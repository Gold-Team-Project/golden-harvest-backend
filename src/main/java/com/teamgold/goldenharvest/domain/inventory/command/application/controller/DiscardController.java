package com.teamgold.goldenharvest.domain.inventory.command.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.inventory.command.application.dto.DiscardItemRequest;
import com.teamgold.goldenharvest.domain.inventory.command.application.service.DiscardService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DiscardController {

	private final DiscardService discardService;

	@PostMapping("/discard")
	public ResponseEntity<ApiResponse<?>> discardItem(@RequestBody @Validated DiscardItemRequest discardItemRequest) {
		return ResponseEntity.ok(ApiResponse.success(discardService.discardItem(discardItemRequest)));
	}
}
