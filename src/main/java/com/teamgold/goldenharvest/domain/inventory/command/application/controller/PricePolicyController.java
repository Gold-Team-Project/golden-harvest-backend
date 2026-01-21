package com.teamgold.goldenharvest.domain.inventory.command.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.inventory.command.application.dto.PricePolicyRequest;
import com.teamgold.goldenharvest.domain.inventory.command.application.service.PricePolicyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequestMapping("/api")
@RequiredArgsConstructor
public class PricePolicyController {

	private final PricePolicyService pricePolicyService;

	@PostMapping("/price-policy")
	public ResponseEntity<ApiResponse<?>> registerPricePolicy(@Valid @RequestBody PricePolicyRequest pricePolicyRequest) {
		return ResponseEntity.ok(ApiResponse.success(pricePolicyService.registerPricePolicy(pricePolicyRequest)));
	}
}
