package com.teamgold.goldenharvest.domain.purchases.command.application.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderCreatedEvent {
    String purchaseOrderId;
    String skuNo;
    String createdAt;
    int quantity;
}
