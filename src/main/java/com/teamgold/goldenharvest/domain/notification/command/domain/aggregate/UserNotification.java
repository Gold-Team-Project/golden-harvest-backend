package com.teamgold.goldenharvest.domain.notification.command.domain.aggregate;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_notification_id", nullable = false)
    private Long userNotificationId;

    // 기본 문구(템플릿) 참조: type이 PK라서 FK도 type 컬럼으로 거는 게 깔끔함
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_type", nullable = false)
    private NotificationTemplate templateType;

    // 수신자 (너희 User PK 타입에 맞춰서 String/Long 선택)
    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Builder.Default
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @CreationTimestamp
    @Column(name = "received_at", nullable = false, updatable = false)
    private LocalDateTime receivedAt;

    public void markAsRead(LocalDateTime now) {
        this.isRead = true;
        this.readAt = now;
    }
}
