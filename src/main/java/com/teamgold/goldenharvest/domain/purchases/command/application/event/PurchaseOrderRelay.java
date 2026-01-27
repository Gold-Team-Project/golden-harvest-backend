package com.teamgold.goldenharvest.domain.purchases.command.application.event;


import com.teamgold.goldenharvest.common.broker.KafkaProducerHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
@Slf4j
public class PurchaseOrderRelay {

    private final KafkaProducerHelper kafkaProducer;

    // @Async
    @EventListener
    public void handle(PurchaseOrderCreatedEvent event) {
        log.info("Received PO created event");

        kafkaProducer.send("purchase.order.created", event.getPurchaseOrderId(), event, (ex) -> {
            // Todo: 전송 실패 시 처리
            log.error("Kafka failed: {}", ex.getMessage());
        });
    }
}
