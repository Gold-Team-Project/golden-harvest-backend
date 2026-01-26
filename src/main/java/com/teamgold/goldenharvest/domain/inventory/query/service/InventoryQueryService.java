package com.teamgold.goldenharvest.domain.inventory.query.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.teamgold.goldenharvest.domain.inventory.query.dto.AvailableItemResponse;
import com.teamgold.goldenharvest.domain.inventory.query.dto.InboundResponse;
import com.teamgold.goldenharvest.domain.inventory.query.dto.ItemResponse;
import com.teamgold.goldenharvest.domain.inventory.query.dto.OutboundResponse;
import com.teamgold.goldenharvest.domain.inventory.query.mapper.InboundMapper;
import com.teamgold.goldenharvest.domain.inventory.query.mapper.LotMapper;
import com.teamgold.goldenharvest.domain.inventory.query.mapper.OutboundMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryQueryService {

	private final LotMapper lotMapper;
	private final InboundMapper inboundMapper;
	private final OutboundMapper outboundMapper;

	public List<AvailableItemResponse> getAllAvailableItem(
		Integer page,
		Integer size,
		String skuNo) {
		int limit = size;
		int offset = (page - 1) * limit;

		return lotMapper.findAllAvailableItems(limit, offset, skuNo);
	}

	public List<ItemResponse> getAllItem(
		Integer page,
		Integer size,
		String skuNo,
		LocalDate startDate,
		LocalDate endDate) {
		int limit = size;
		int offset = (page - 1) * limit;

		if (isInvalidDate(startDate, endDate)) {
			startDate = LocalDate.now().minusWeeks(1);
			endDate = LocalDate.now(); // 날짜 필터링 기본 설정 (최근 일주일)
		}

		return lotMapper.findAllItems(limit, offset, skuNo, startDate, endDate);
	}

	public List<InboundResponse> getInbounds(
		Integer page,
		Integer size,
		String skuNo,
		LocalDate startDate,
		LocalDate endDate) {

		int limit = size;
		int offset = (page - 1) * limit;

		if (isInvalidDate(startDate, endDate)) {
			startDate = LocalDate.now().minusWeeks(1);
			endDate = LocalDate.now(); // 날짜 필터링 기본 설정 (최근 일주일)
		}

		return inboundMapper.findAllInbounds(
			limit,
			offset,
			skuNo,
			startDate,
			endDate
		);
	}

	public List<OutboundResponse> getOutbounds(
		Integer page,
		Integer size,
		String skuNo,
		String lotNo,
		LocalDate startDate,
		LocalDate endDate
	) {
		int limit = size;
		int offset = (page - 1) * limit;

		if (isInvalidDate(startDate, endDate)) {
			startDate = LocalDate.now().minusWeeks(1);
			endDate = LocalDate.now(); // 날짜 필터링 기본 설정 (최근 일주일)
		}

		return outboundMapper.findAllOutbounds(
			limit,
			offset,
			skuNo,
			lotNo,
			startDate,
			endDate
		);
	}

	private boolean isInvalidDate(LocalDate startDate,  LocalDate endDate) {
		if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
			return true;
		}
		else return startDate.isBefore(endDate);
	}
}
