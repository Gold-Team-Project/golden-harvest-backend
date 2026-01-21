package com.teamgold.goldenharvest.domain.notification.command.application.controller;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.notification.command.application.service.NotificationCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationCommandController {

    private final NotificationCommandService notificationCommandService;

    @DeleteMapping("/deleteAll")
        public ResponseEntity<ApiResponse<Void>> deleteAllNotification(
            @RequestParam String userEmail
    ) {
        notificationCommandService.DeleteAllNotification(userEmail);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable Long notificationId
    ) {
        notificationCommandService.DeleteNotificationById(notificationId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long notificationId
    ) {
        notificationCommandService.markAsRead(notificationId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
