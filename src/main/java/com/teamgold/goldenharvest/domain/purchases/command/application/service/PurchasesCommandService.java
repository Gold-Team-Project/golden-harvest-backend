package com.teamgold.goldenharvest.domain.purchases.command.application.service;

import com.teamgold.goldenharvest.domain.notification.command.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchasesCommandService {

    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Long createPurchaseOrder()


}
