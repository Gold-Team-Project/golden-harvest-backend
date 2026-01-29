package com.teamgold.goldenharvest.domain.notification.query.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserNotificationDTO {

    private Long userNotificationId;
    private NotificationTemplateDTO notificationTemplate;
    private String userEmail;
    private boolean isRead;
    private LocalDateTime readAt;
    private LocalDateTime receivedAt;

}
