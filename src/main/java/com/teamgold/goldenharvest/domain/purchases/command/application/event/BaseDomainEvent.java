package com.teamgold.goldenharvest.domain.purchases.command.application.event;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseDomainEvent implements DomainEvent {
    private final UUID eventId= UUID.randomUUID();
    private final Instant occurredAt= Instant.now();

    @Override
    public UUID eventId() {return eventId; }
    @Override
    public Instant occurredAt() {return occurredAt; }
}