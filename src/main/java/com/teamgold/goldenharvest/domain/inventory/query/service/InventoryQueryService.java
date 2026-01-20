package com.teamgold.goldenharvest.domain.inventory.query.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.inventory.query.dto.AvailableItemResponse;
import com.teamgold.goldenharvest.domain.inventory.query.dto.InboundResponse;
import com.teamgold.goldenharvest.domain.inventory.query.dto.OutboundResponse;
import com.teamgold.goldenharvest.domain.inventory.query.mapper.InboundMapper;
import com.teamgold.goldenharvest.domain.inventory.query.mapper.LotMapper;
import com.teamgold.goldenharvest.domain.inventory.query.mapper.OutboundMapper;

import jakarta.validation.ConstraintViolationException;
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

	public List<InboundResponse> getInbounds(
		Integer page,
		Integer size,
		String skuNo,
		LocalDate startDate,
		LocalDate endDate) {

		int limit = size;
		int offset = (page - 1) * limit;

		if (!validateDate(startDate, endDate)) {
			throw new BusinessException(ErrorCode.INVALID_REQUEST);
		}

		if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
			startDate = LocalDate.now().minusWeeks(1);
			endDate = LocalDate.now();
		} // startDate와 endDate의 default 설정

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

		if (!validateDate(startDate, endDate)) {
			throw new BusinessException(ErrorCode.INVALID_REQUEST);
		}

		if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
			startDate = LocalDate.now().minusWeeks(1);
			endDate = LocalDate.now();
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

	private boolean validateDate(LocalDate startDate,  LocalDate endDate) {
		return startDate.isAfter(endDate);
	}
}
