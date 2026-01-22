package com.teamgold.goldenharvest.domain.inventory.query.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.inventory.query.service.DiscardQueryService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequestMapping("/api")
@RequiredArgsConstructor
public class DiscardQueryController {

	private final DiscardQueryService discardQueryService;

	/*
	* 폐기 기록을 반환하는 endpoint
	* page, size, skuNo, status, startDate, endDate
	* 위 파라미터로 필터링 가능하다
	* 날짜의 경우 명시되지 않았다면 최근 일주일 사이의 기록을 조회한다
	* 날짜별 최신순으로 정렬된다
	 */
	@GetMapping("/discard")
	public ResponseEntity<ApiResponse<?>> getAllDiscard(
		@RequestParam(name = "page", defaultValue = "1") @Min(1) Integer page,
		@RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(50) Integer size,
		@RequestParam(name = "skuNo", required = false) String skuNo,
		@RequestParam(name = "status", required = false) String discardStatus,
		@RequestParam(name = "startDate", required = false) LocalDate startDate,
		@RequestParam(name = "endDate", required = false) LocalDate endDate
	) {
		return ResponseEntity.ok(ApiResponse.success(discardQueryService.getAllDiscard(
			page,
			size,
			skuNo,
			discardStatus,
			startDate,
			endDate
		)));
	}
}
