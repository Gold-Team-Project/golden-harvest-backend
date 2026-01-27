package com.teamgold.goldenharvest.domain.inventory.query.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.inventory.query.service.PricePolicyQueryService;

import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequestMapping("/api")
@RequiredArgsConstructor
public class PricePolicyQueryController {

	private final PricePolicyQueryService pricePolicyQueryService;

	/*
	* 가격 정책 리스트를 조회하는 endpoint
	 */
	@GetMapping("/price-policy")
	public ResponseEntity<ApiResponse<?>> getAllPricePolicy() {
		return ResponseEntity.ok(ApiResponse.success(pricePolicyQueryService.getAllPricePolicy()));
	}
}
