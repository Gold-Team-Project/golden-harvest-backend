package com.teamgold.goldenharvest.notification;

import com.teamgold.goldenharvest.domain.notification.command.application.event.SignupPendingEvent;
import com.teamgold.goldenharvest.domain.notification.command.application.service.NotificationCommandService;
import com.teamgold.goldenharvest.domain.notification.command.domain.aggregate.UserNotification;
import com.teamgold.goldenharvest.domain.notification.command.domain.repository.UserNotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class NotificationCommandServiceTest {
    @InjectMocks
    private NotificationCommandService notificationCommandService;

    @Mock
    private UserNotificationRepository userNotificationRepository;

    @Test
    void notificationReceived_savesUserNotification() {
        // given
        String userEmail = "test@teamgold.com";
        SignupPendingEvent event = new SignupPendingEvent(userEmail);

        // when
        notificationCommandService.NotificationRecieved(event);

        // then
        ArgumentCaptor<UserNotification> captor = ArgumentCaptor.forClass(UserNotification.class);
        then(userNotificationRepository).should(times(1)).save(captor.capture());

        UserNotification savedNotification = captor.getValue();
        assertThat(savedNotification.getUserEmail()).isEqualTo(userEmail);
    }

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

    @Test
    void markAsRead_marksUnreadNotificationAsRead() {
        // given
        Long notificationId = 1L;
        UserNotification unreadNotification = UserNotification.builder()
                .userEmail("test@teamgold.com")
                .build();

        given(userNotificationRepository.findByUserNotificationId(notificationId))
                .willReturn(Optional.of(unreadNotification));

        // when
        notificationCommandService.markAsRead(notificationId);

        // then
        then(userNotificationRepository).should(times(1))
                .findByUserNotificationId(notificationId);
        assertThat(unreadNotification.isRead()).isTrue();
        assertThat(unreadNotification.getReadAt()).isNotNull();
    }

    @Test
    void markAsRead_skipsAlreadyReadNotification() {
        // given
        Long notificationId = 1L;
        UserNotification readNotification = UserNotification.builder()
                .userEmail("test@teamgold.com")
                .build();
        readNotification.markAsRead(java.time.LocalDateTime.now());

        given(userNotificationRepository.findByUserNotificationId(notificationId))
                .willReturn(Optional.of(readNotification));

        // when
        notificationCommandService.markAsRead(notificationId);

        // then
        then(userNotificationRepository).should(times(1))
                .findByUserNotificationId(notificationId);
        assertThat(readNotification.isRead()).isTrue();
    }

    @Test
    void markAsRead_throwsEntityNotFoundException_whenNotificationNotFound() {
        // given
        Long notificationId = 999L;
        given(userNotificationRepository.findByUserNotificationId(notificationId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> notificationCommandService.markAsRead(notificationId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("UserNotification not found: " + notificationId);
    }
}
