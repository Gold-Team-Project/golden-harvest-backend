package com.teamgold.goldenharvest.domain.sales.command.application.event;

import com.teamgold.goldenharvest.domain.inventory.command.application.dto.SalesOrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SalesOrderEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishSalesOrderEvent(SalesOrderEvent salesOrderEvent) {
        eventPublisher.publishEvent(salesOrderEvent);
    }
}
