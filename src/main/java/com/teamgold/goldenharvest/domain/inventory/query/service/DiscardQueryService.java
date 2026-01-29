package com.teamgold.goldenharvest.domain.inventory.query.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import com.teamgold.goldenharvest.domain.inventory.query.dto.DiscardRateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.inventory.query.dto.DiscardLossResponse;
import com.teamgold.goldenharvest.domain.inventory.query.dto.DiscardResponse;
import com.teamgold.goldenharvest.domain.inventory.query.dto.DiscardVolumeResponse;
import com.teamgold.goldenharvest.domain.inventory.query.mapper.DiscardMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DiscardQueryService {

	private final DiscardMapper discardMapper;


	public List<DiscardResponse> getAllDiscard(
		Integer page,
		Integer size,
		String skuNo,
		String discardStatus,
		LocalDate startDate,
		LocalDate endDate
	) {
		if (isInvalidDate(startDate, endDate)) {
			startDate = LocalDate.now().minusWeeks(1);
			endDate = LocalDate.now(); // 날짜 필터링 기본 설정 (최근 일주일)
		}

		if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
			startDate = LocalDate.now().minusWeeks(1);
			endDate = LocalDate.now();
		}

		Integer limit = size;
		Integer offset = (page - 1) * limit;

		LocalDateTime startDateTime = startDate.atStartOfDay();
		LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

		return discardMapper.findAllDiscard(
			limit,
			offset,
			skuNo,
			discardStatus,
			startDateTime,
			endDateTime
		);
	}

	public DiscardVolumeResponse getDiscardVolume() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime thisMonthStart = now.withDayOfMonth(1).with(LocalTime.MIN);
		LocalDateTime lastMonthStart = thisMonthStart.minusMonths(1);
		LocalDateTime lastMonthUntilNow = now.minusMonths(1);

		return discardMapper.findDiscardVolume(
			lastMonthStart,
			lastMonthUntilNow,
			thisMonthStart,
			now
		);
	}

	public DiscardLossResponse getDiscardLoss() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime thisMonthStart = now.withDayOfMonth(1).with(LocalTime.MIN);
		LocalDateTime lastMonthStart = thisMonthStart.minusMonths(1);
		LocalDateTime lastMonthUntilNow = now.minusMonths(1);

		return discardMapper.findDiscardLoss(
			lastMonthStart,
			lastMonthUntilNow,
			thisMonthStart,
			now
		);
	}

	public List<DiscardRateResponse> getDiscardRate() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime thisMonthStart = now.withDayOfMonth(1).with(LocalTime.MIN);

		return discardMapper.findDiscardRate(
				thisMonthStart,
				now
		);
	}

	private boolean isInvalidDate(LocalDate startDate, LocalDate endDate) {
		if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
			return true;
		}
		else return startDate.isAfter(endDate);
	}
}
