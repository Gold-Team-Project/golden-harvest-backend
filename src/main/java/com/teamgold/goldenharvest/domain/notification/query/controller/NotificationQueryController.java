package com.teamgold.goldenharvest.domain.notification.query.controller;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.notification.query.dto.request.NotificationSearchRequest;
import com.teamgold.goldenharvest.domain.notification.query.dto.response.NotificationListResponse;
import com.teamgold.goldenharvest.domain.notification.query.service.NotificationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationQueryController {

    private final NotificationQueryService notificationQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse<NotificationListResponse>> getNotificationList(
            NotificationSearchRequest NotificationSearchRequest
    ){
        NotificationListResponse response = notificationQueryService.getNotificationList(NotificationSearchRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{userEmail}/count")
    public ResponseEntity<ApiResponse<Long>> getCountUnreadNotifications(
            @PathVariable String userEmail
    ){
        Long response = notificationQueryService.countUnreadNotifications(userEmail);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


}
