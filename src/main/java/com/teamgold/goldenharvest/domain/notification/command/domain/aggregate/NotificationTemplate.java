package com.teamgold.goldenharvest.domain.notification.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_notification_template")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class NotificationTemplate {

    @Id
    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @Column(name = "title", length = 255)
    private String title;

    @Lob
    @Column(name = "body", columnDefinition = "TEXT")
    private String body;
}
