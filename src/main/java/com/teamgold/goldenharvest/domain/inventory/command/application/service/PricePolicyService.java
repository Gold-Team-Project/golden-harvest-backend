package com.teamgold.goldenharvest.domain.inventory.command.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.inventory.command.application.dto.PricePolicyRequest;
import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.PricePolicy;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.PricePolicyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PricePolicyService {

	private final PricePolicyRepository pricePolicyRepository;

	public String registerPricePolicy(PricePolicyRequest pricePolicyRequest) {
		List<PricePolicy> policies = pricePolicyRepository.findBySkuNo(pricePolicyRequest.getSkuNo());

		if (!policies.isEmpty()) {
			throw new BusinessException(ErrorCode.INVALID_REQUEST);
		}

		return pricePolicyRepository.save(
			PricePolicy.builder()
				.skuNo(pricePolicyRequest.getSkuNo())
				.marginRate(pricePolicyRequest.getMarginRate())
				.build()
		).getSkuNo();
	}
}
