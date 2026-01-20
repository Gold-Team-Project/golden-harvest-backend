package com.teamgold.goldenharvest.domain.inventory.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.teamgold.goldenharvest.domain.inventory.query.dto.AvailableItemResponse;
import com.teamgold.goldenharvest.domain.inventory.query.mapper.LotMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LotQueryService {

	private final LotMapper lotMapper;

	public List<AvailableItemResponse> getAllAvailableItem(Integer page, Integer size, String skuNo) {
		int limit = size;
		int offset = (page - 1) * limit;

		return lotMapper.findAllAvailableItems(limit, offset, skuNo);
	}
}
