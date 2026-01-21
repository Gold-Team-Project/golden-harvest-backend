package com.teamgold.goldenharvest.domain.notification.command.domain.repository;

import com.teamgold.goldenharvest.domain.notification.command.domain.aggregate.UserNotification;

import java.util.List;
import java.util.Optional;

public interface UserNotificationRepository {

    Optional<UserNotification> findByUserNotificationId(Long id);
    UserNotification save(UserNotification userNotification);
    List<UserNotification> findAllByUserEmail(String userEmail);
    void deleteAllByUserEmail(String userEmail);
    void deleteByUserNotificationId(Long id);
}
