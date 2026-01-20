package com.teamgold.goldenharvest.domain.inventory.query.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.inventory.query.service.LotQueryService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LotQueryController {

	private final LotQueryService lotQueryService;

	@GetMapping("/item")
	public ResponseEntity<ApiResponse<?>> getAvailableItemList(
		@RequestParam(name = "page", defaultValue = "1") @Min(1) Integer page,
		@RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(50) Integer size,
		@RequestParam(name = "sku") String skuNo
	) {
		return ResponseEntity.ok(ApiResponse.success(lotQueryService.getAllAvailableItem(page, size, skuNo)));
	}

	@GetMapping("/inbound")
	public ResponseEntity<ApiResponse<?>> getAllInboundList() {
		return null;
	}

	@GetMapping("/outbound")
	public ResponseEntity<ApiResponse<?>> getAllOutboundList() {
		// Todo
		return null;
	}
}
