package com.teamgold.goldenharvest.domain.inventory.query.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.PricePolicy;
import com.teamgold.goldenharvest.domain.inventory.query.dto.PricePolicyResponse;
import com.teamgold.goldenharvest.domain.inventory.query.mapper.PricePolicyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PricePolicyQueryService {

	private final PricePolicyMapper pricePolicyMapper;

	public List<PricePolicyResponse> getAllPricePolicy() {
		return pricePolicyMapper.findAllPricePolicies();
	}
}
