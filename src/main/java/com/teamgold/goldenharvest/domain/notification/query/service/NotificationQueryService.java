package com.teamgold.goldenharvest.domain.notification.query.service;

import com.teamgold.goldenharvest.domain.notification.command.domain.repository.NotificationRepository;
import com.teamgold.goldenharvest.domain.notification.query.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationQueryService {

    private final NotificationMapper notificationMapper;
}
