package com.teamgold.goldenharvest.domain.inventory.command.application.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.teamgold.goldenharvest.domain.sales.command.application.event.dto.SalesOrderEvent;
import com.teamgold.goldenharvest.domain.inventory.command.application.event.dto.SalesOrderResultEvent;
import com.teamgold.goldenharvest.domain.inventory.command.application.service.LotService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SalesOrderEventListener {

	private final LotService lotService;
	private final ApplicationEventPublisher eventPublisher;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleSalesOrder(SalesOrderEvent salesOrderEvent) {
		log.info("판매 주문 event 수신 완료");

		try {
			lotService.consumeLot(salesOrderEvent);

			eventPublisher.publishEvent(SalesOrderResultEvent.builder()
					.salesOrderItemId(salesOrderEvent.salesOrderItemId())
					.status("SUCCESS")
					.build());

			log.info("판매 주문 처리 완료");
		} catch (Exception e) {
			// Transaction rollback 처리
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

			eventPublisher.publishEvent(SalesOrderResultEvent.builder()
							.salesOrderItemId(salesOrderEvent.salesOrderItemId())
							.status("FAIL")
							.build());
			log.info("판매 주문 실패");
		}
	}
}
