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
    private Long user_notification_id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_read",nullable = false)
    private Boolean isRead;

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
