package com.teamgold.goldenharvest.domain.notification.command.infrastructure.repository;

import com.teamgold.goldenharvest.domain.notification.command.domain.aggregate.UserNotification;
import com.teamgold.goldenharvest.domain.notification.command.domain.repository.UserNotificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserNotificationRepository extends UserNotificationRepository, JpaRepository<UserNotification, Long> {

}
