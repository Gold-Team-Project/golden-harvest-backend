package com.teamgold.goldenharvest.domain.purchases.command.application.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(com.teamgold.goldenharvest.domain.purchases.command.application.event.DomainEvent event) {
        publisher.publishEvent(event);
    }
}
