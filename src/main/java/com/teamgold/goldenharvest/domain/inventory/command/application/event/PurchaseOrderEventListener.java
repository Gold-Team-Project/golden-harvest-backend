package com.teamgold.goldenharvest.domain.inventory.command.application.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.teamgold.goldenharvest.domain.inventory.command.application.dto.PurchaseOrderEvent;
import com.teamgold.goldenharvest.domain.inventory.command.application.service.InboundService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PurchaseOrderEventListener {

	private final InboundService inboundService;

	// purchaseOrderEvent를 observe하는 event 처리 메소드이다
	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handlePurchaseOrder(PurchaseOrderEvent purchaseOrderEvent) {
		log.info("구매 주문 이벤트 수신 완료. 객체: {}", purchaseOrderEvent);

		String createdLotNo = inboundService.processInbound(purchaseOrderEvent);

		log.info("재고 등록 완료. Lot 번호: {}", createdLotNo);
	}
}