package com.teamgold.goldenharvest.domain.notification.command.infrastructure.repository;

import com.teamgold.goldenharvest.domain.notification.command.domain.aggregate.NotificationType;
import com.teamgold.goldenharvest.domain.notification.command.domain.repository.NotificationTypeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNotificationTypeRepository extends NotificationTypeRepository, JpaRepository<NotificationType,Long> {
}
