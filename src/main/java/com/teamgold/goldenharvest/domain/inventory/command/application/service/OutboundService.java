package com.teamgold.goldenharvest.domain.inventory.command.application.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamgold.goldenharvest.domain.sales.command.application.event.dto.SalesOrderEvent;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.IdGenerator;
import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.Lot;
import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.Outbound;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.OutboundRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OutboundService {

	private final OutboundRepository outboundRepository;

	@Transactional
	public String processOutbound(Lot lot, int consumedQuantity, SalesOrderEvent salesOrderEvent) {
		Outbound currentOutbound = Outbound.builder()
			.outboundId(IdGenerator.createId("out"))
			.salesOrderItemId(salesOrderEvent.salesOrderItemId())
			.lot(lot)
			.outboundDate(LocalDate.now())
			.quantity(consumedQuantity)
			.outboundPrice(salesOrderEvent.salesPrice())
			.build();

		return outboundRepository.save(currentOutbound).getOutboundId();
	}
}
