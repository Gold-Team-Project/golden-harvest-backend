package com.teamgold.goldenharvest.domain.master.command.application.event;


import com.teamgold.goldenharvest.domain.master.command.application.event.dto.ItemMasterUpdatedEvent;
import com.teamgold.goldenharvest.domain.master.command.application.event.dto.ItemOriginPriceUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MasterDataEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishItemMasterUpdatedEvent(ItemMasterUpdatedEvent event) {
        eventPublisher.publishEvent(event);
    }

    public void publishItemOriginPriceUpdatedEvent(ItemOriginPriceUpdateEvent event) {
        eventPublisher.publishEvent(event);
    }
}
