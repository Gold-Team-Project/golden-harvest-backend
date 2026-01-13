package com.teamgold.goldenharvest.domain.purchases.command.application.event;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
