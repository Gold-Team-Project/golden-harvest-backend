package com.teamgold.goldenharvest.domain.purchases.command.application.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class PurchaseOrderCreatedEvent extends BaseDomainEvent {

    private final String purchaseOrderId;
    private final LocalDate createdAt;
    private final int quantity;

    @Override
    public String type(){
        return "PurchaseOrderCreated";
    }
}
