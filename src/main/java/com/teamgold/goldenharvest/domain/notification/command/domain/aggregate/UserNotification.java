package com.teamgold.goldenharvest.domain.notification.command.domain.aggregate;

import com.teamgold.goldenharvest.domain.user.command.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_user_notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserNotification {

    @Id
    @Column(name = "user_notification_id", nullable = false, length = 255)
    private Long userNotificationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Builder.Default
    @Column(name = "is_read",nullable = false)
    private Boolean isRead = false;

    @Column(name = "read_at",nullable = true)
    private LocalDateTime readAt;

    @CreationTimestamp
    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    public void markAsRead(LocalDateTime now) {
        this.isRead = true;
        this.readAt = now;
    }

}
