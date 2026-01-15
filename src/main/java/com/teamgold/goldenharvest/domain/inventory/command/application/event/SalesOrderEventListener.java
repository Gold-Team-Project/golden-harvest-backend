package com.teamgold.goldenharvest.domain.inventory.command.application.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.teamgold.goldenharvest.domain.inventory.command.application.dto.PurchaseOrderEvent;
import com.teamgold.goldenharvest.domain.inventory.command.application.dto.SalesOrderEvent;
import com.teamgold.goldenharvest.domain.inventory.command.application.service.OutboundService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SalesOrderEventListener {

	private final OutboundService outboundService;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleSalesOrder(SalesOrderEvent salesOrderEvent) {
		log.info("판매 주문 event 수신 완료");

		outboundService.processOutbound(salesOrderEvent);
		// Todo: 재고 확인 실패 시 처리 로직 구현
		log.info("판매 주문 처리 완료");
	}
}
