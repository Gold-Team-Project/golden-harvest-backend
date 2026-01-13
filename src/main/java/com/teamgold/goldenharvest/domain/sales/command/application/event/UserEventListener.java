package com.teamgold.goldenharvest.domain.sales.command.application.event;

import com.teamgold.goldenharvest.domain.sales.command.application.service.SalesOrderCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final SalesOrderCommandService salesOrderCommandService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleUserUpdate(UserUpdatedEvent event) {
        log.info("Received user update event for user ID: {}", event.getUserId());
        // SalesOrder 테이블의 userEmail을 업데이트하는 로직 호출
        salesOrderCommandService.updateUserEmail(event.getUserId(), event.getNewEmail());
    }
}
