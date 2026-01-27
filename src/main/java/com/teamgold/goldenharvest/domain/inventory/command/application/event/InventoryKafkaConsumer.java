package com.teamgold.goldenharvest.domain.inventory.command.application.event;

import com.teamgold.goldenharvest.domain.inventory.command.application.dto.PurchaseOrderEvent;
import com.teamgold.goldenharvest.domain.inventory.command.application.service.InboundService;
import com.teamgold.goldenharvest.domain.purchases.command.application.event.PurchaseOrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InventoryKafkaConsumer {

    private final InboundService inboundService;

    @KafkaListener(topics = "purchase.order.created", groupId = "golden.harvest.inventory.processor")
    public void consume(PurchaseOrderCreatedEvent event) {
        log.info("kafka consume success");
        log.info("PO ID: " + event.getPurchaseOrderId());


        inboundService.processInbound(PurchaseOrderEvent.builder()
                .skuNo(event.getSkuNo())
                .quantity(event.getQuantity())
                .purchaseOrderId(event.getPurchaseOrderId())
                .build());
    }
}
