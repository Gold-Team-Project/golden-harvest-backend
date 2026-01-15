package com.teamgold.goldenharvest.domain.notification.query.service;

import com.teamgold.goldenharvest.common.response.Pagination;
import com.teamgold.goldenharvest.domain.notification.query.dto.request.NotificationSearchRequest;
import com.teamgold.goldenharvest.domain.notification.query.dto.response.NotificationListResponse;
import com.teamgold.goldenharvest.domain.notification.query.dto.response.UserNotificationDTO;
import com.teamgold.goldenharvest.domain.notification.query.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationQueryService {

    private final NotificationMapper notificationMapper;

    @Transactional(readOnly = true)
    public NotificationListResponse getNotificationList(NotificationSearchRequest request){
        List<UserNotificationDTO> userNotifications = notificationMapper.selectUserNotifications(request);

        long totalItems = notificationMapper.countNotifications(request);

        int page = request.getPage();
        int size = request.getSize();

        return NotificationListResponse.builder()
                .notifications(userNotifications)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPages((int) Math.ceil((double) totalItems / size))
                        .totalItems(totalItems)
                        .build())
                .build();
    }
}
