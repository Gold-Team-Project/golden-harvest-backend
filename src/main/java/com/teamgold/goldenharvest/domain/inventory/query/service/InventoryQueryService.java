package com.teamgold.goldenharvest.domain.inventory.query.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
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
		String itemName,
		LocalDate startDate,
		LocalDate endDate,
		String status) {

		if (startDate == null || endDate == null) {
			startDate = LocalDate.now().minusWeeks(1);
			endDate = LocalDate.now();
		}

		if (startDate.isAfter(endDate)) {
			throw new BusinessException(ErrorCode.INVALID_DATE_FILTER);
		}

		int limit = size;
		int offset = (page - 1) * limit;


		return lotMapper.findAllItems(limit, offset, itemName, startDate, endDate, status);
	}

	public List<InboundResponse> getInbounds(
		Integer page,
		Integer size,
		String skuNo,
		LocalDate startDate,
		LocalDate endDate) {

		int limit = size;
		int offset = (page - 1) * limit;

		if (startDate == null || endDate == null) {
			startDate = LocalDate.now().minusWeeks(1);
			endDate = LocalDate.now();
		}

		if (startDate.isAfter(endDate)) {
			throw new BusinessException(ErrorCode.INVALID_DATE_FILTER);
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

		if (startDate == null || endDate == null) {
			startDate = LocalDate.now().minusWeeks(1);
			endDate = LocalDate.now();
		}

		if (startDate.isAfter(endDate)) {
			throw new BusinessException(ErrorCode.INVALID_DATE_FILTER);
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
}
