package com.teamgold.goldenharvest.domain.inventory.command.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.inventory.command.application.dto.PurchaseOrderEvent;
import com.teamgold.goldenharvest.domain.inventory.command.application.dto.SalesOrderEvent;
import com.teamgold.goldenharvest.domain.inventory.command.domain.IdGenerator;
import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.Lot;
import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.LotStatus;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.LotRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LotService {

	private final LotRepository lotRepository;
	private final OutboundService outboundService;

	@Transactional
	public String createLot(PurchaseOrderEvent purchaseOrderEvent, String inboundNo) {
		String lotNo = IdGenerator.createId("lot");

		Lot lot = Lot.builder()
			.lotNo(lotNo)
			.inboundId(inboundNo)
			.skuNo(purchaseOrderEvent.skuNo())
			.quantity(purchaseOrderEvent.quantity())
			.inboundDate(LocalDate.now())
			.lotStatus(LotStatus.LotStatusType.AVAILABLE)
			.build();

		return lotRepository.save(lot).getLotNo();
	}

	@Transactional
	public void ConsumeLot(SalesOrderEvent salesOrderEvent) {

		String skuNo = salesOrderEvent.skuNo();
		int quantity = salesOrderEvent.quantity();

		List<Lot> availableItems = lotRepository.findBySkuNoAndLotStatusOrderByInboundDateAsc(skuNo,
			LotStatus.LotStatusType.AVAILABLE);

		// 주문 수량이 재고 수량보다 많을 때 발생 (동시성 문제 OR OUTDATED 재고 정보 참조 등)
		if (availableItems.stream().mapToInt(Lot::getQuantity).sum() < quantity) {
			throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
		}

		int remainingQuantity = quantity;
		for (Lot item : availableItems) {
			if (remainingQuantity <= 0) break;

			int consumed = item.consumeQuantity(remainingQuantity);
			remainingQuantity -= consumed;

			outboundService.processOutbound(item, consumed, salesOrderEvent);
		}
	}
}
