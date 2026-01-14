package com.teamgold.goldenharvest.domain.notification.command.application.service;

import com.teamgold.goldenharvest.domain.notification.command.application.event.SignupPendingEvent;
import com.teamgold.goldenharvest.domain.notification.command.domain.aggregate.UserNotification;
import com.teamgold.goldenharvest.domain.notification.command.domain.repository.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class NotificationCommandService {

    private final UserNotificationRepository userNotificationRepository;

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void NotificationRecieved(SignupPendingEvent e) {
        UserNotification userNotification = UserNotification.builder()
                .userId(e.getUserEmail()).build();
    }

}
