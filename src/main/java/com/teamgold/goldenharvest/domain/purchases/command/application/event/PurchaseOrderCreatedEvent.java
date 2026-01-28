package com.teamgold.goldenharvest.domain.purchases.command.application.event;


import java.time.LocalDate;

public record PurchaseOrderCreatedEvent(
        String purchaseOrderId,
        LocalDate createdAt,
        String skuNo,
        int quantity
) {
}
