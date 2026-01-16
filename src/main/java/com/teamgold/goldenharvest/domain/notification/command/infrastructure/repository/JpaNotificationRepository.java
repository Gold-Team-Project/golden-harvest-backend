package com.teamgold.goldenharvest.domain.notification.command.infrastructure.repository;

import com.teamgold.goldenharvest.domain.notification.command.domain.aggregate.NotificationTemplate;
import com.teamgold.goldenharvest.domain.notification.command.domain.repository.NotificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNotificationRepository extends NotificationRepository, JpaRepository<NotificationTemplate,Long> {
}
