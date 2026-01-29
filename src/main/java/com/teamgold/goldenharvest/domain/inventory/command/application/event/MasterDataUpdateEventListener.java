package com.teamgold.goldenharvest.domain.inventory.command.application.event;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.teamgold.goldenharvest.domain.master.command.application.event.dto.ItemMasterUpdatedEvent;
import com.teamgold.goldenharvest.domain.master.command.application.event.dto.ItemOriginPriceUpdateEvent;
import com.teamgold.goldenharvest.domain.inventory.command.application.service.ItemMasterMirrorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MasterDataUpdateEventListener {

	private final ItemMasterMirrorService itemMasterMirrorService;

	// Master Data의 update를 listen하여 snapshot을 저장하는 event 기반 처리 메소드이다
	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateItemMasterMirror(ItemMasterUpdatedEvent itemMasterUpdatedEvent) {
		log.info("마스터데이터 업데이트 이벤트 수신 완료.");

		itemMasterMirrorService.updateItemMasterMirror(itemMasterUpdatedEvent);

		log.info("마스터데이터 mirror 업데이트 완료");
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateItemPrice(ItemOriginPriceUpdateEvent itemOriginPriceUpdateEvent) {
		log.info("원가 업데이트 이벤트 수신 완료.");

		itemMasterMirrorService.updateOriginPrice(itemOriginPriceUpdateEvent);

		log.info("원가 업데이트 이벤트 처리 완료");
	}
}
