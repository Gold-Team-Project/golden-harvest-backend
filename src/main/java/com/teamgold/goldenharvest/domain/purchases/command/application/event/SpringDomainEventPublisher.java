package com.teamgold.goldenharvest.domain.purchases.command.application.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(PurchaseOrderCreatedEvent event) {
        publisher.publishEvent(event);
    }
}
