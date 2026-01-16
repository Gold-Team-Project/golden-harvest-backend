package com.teamgold.goldenharvest.domain.notification.command.application.controller;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.notification.command.application.service.NotificationCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationCommandController {

    private final NotificationCommandService notificationCommandService;

    @DeleteMapping("/{userEmail}")
    public ResponseEntity<ApiResponse<Void>> deleteAllNotification(
            @PathVariable String userEmail
    ) {
        notificationCommandService.DeleteAllNotification(userEmail);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{notificationid}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable Long notificationid
    ) {
        notificationCommandService.DeleteNotificationById(notificationid);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
