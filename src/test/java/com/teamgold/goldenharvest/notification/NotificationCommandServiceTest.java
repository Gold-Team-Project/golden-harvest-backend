package com.teamgold.goldenharvest.notification;

import com.teamgold.goldenharvest.domain.notification.command.application.service.NotificationCommandService;
import com.teamgold.goldenharvest.domain.notification.command.domain.repository.UserNotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class NotificationCommandServiceTest {
    @InjectMocks
    private NotificationCommandService notificationCommandService;

    @Mock
    private UserNotificationRepository userNotificationRepository;

    @Test
    void deleteAllNotification_callsRepository() {
        // given
        String userEmail = "alpha@teamgold.com";

        // when
        notificationCommandService.DeleteAllNotification(userEmail);

        // then
        then(userNotificationRepository).should(times(1))
                .deleteAllByUserEmail(userEmail);
    }

    @Test
    void deleteNotificationById_callsRepository() {
        // given
        Long notificationId = 1L;

        // when
        notificationCommandService.DeleteNotificationById(notificationId);

        // then
        then(userNotificationRepository).should(times(1))
                .deleteByUserNotificationId(notificationId);
    }
}
