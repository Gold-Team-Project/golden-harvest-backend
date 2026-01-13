package com.teamgold.goldenharvest.domain.inventory.command.application.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamgold.goldenharvest.common.idempotent.BloomFilterManager;
import com.teamgold.goldenharvest.domain.inventory.command.application.dto.PurchaseOrderEvent;
import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.Inbound;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.InboundRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InboundService {

	private final InboundRepository inboundRepository;
	private final LotService lotService;
	private final BloomFilterManager bloomFilterManager;

	public String processInbound(PurchaseOrderEvent purchaseOrderEvent) {
		// idempotency validation
		String eventType = "purchaseOrderEvent";
		boolean isFirstRequest = bloomFilterManager.isFirstRequest(eventType, purchaseOrderEvent.purchaseOrderId());

		if (!isFirstRequest) {
			inboundRepository.findByPurchaseOrderItemId(purchaseOrderEvent.purchaseOrderId())
				// Todo: 적절한 business exception으로 처리 변경
				.orElseThrow(IllegalArgumentException::new);
		}

		// Todo: purchaseOrderEvent내 데이터 정합성 검증 로직
		// Todo: inbound data 구성
		Inbound inbound = Inbound.builder().build(); // purchaseOrderEvent 데이터를 이용하여 Inbound 생성

		inboundRepository.save(inbound);

		return lotService.createLot(purchaseOrderEvent);
	}
}
