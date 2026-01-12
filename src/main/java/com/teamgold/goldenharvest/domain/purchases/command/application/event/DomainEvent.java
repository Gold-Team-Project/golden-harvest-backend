package com.teamgold.goldenharvest.domain.purchases.command.application.event;
import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
    UUID eventId();          // 이벤트 고유 ID (idempotency/trace)
    Instant occurredAt();    // 발생 시각(UTC)
    String type();           // 이벤트 타입 (명시적 라우팅/버저닝)
}