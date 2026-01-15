package com.teamgold.goldenharvest.domain.inventory.command.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamgold.goldenharvest.domain.inventory.command.application.dto.SalesOrderEvent;
import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.Outbound;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.OutboundRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OutboundService {

	private final OutboundRepository outboundRepository;
	private final LotService lotService;

	@Transactional
	public String processOutbound(SalesOrderEvent salesOrderEvent) {
		// Todo: 출고 이력 생성 및 outboundId 반환

		return null;
	}
}
