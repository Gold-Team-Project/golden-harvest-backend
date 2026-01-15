package com.teamgold.goldenharvest.domain.notification.command.domain.repository;

import com.teamgold.goldenharvest.domain.notification.command.domain.aggregate.UserNotification;

import java.util.List;

public interface UserNotificationRepository {

    UserNotification save(UserNotification userNotification);
    List<UserNotification> findAllByUserEmail(String userEmail);

}
