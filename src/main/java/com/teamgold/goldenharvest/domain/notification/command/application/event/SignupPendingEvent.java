package com.teamgold.goldenharvest.domain.notification.command.application.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignupPendingEvent extends BaseDomainEvent {

    private final String userEmail;

    @Override
    public String type(){
        return "PurchaseOrderCreated";
    }
}
