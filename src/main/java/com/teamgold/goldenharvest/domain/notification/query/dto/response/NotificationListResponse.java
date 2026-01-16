package com.teamgold.goldenharvest.domain.notification.query.dto.response;


import com.teamgold.goldenharvest.common.response.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NotificationListResponse {
    List<UserNotificationDTO> notifications;
    Pagination pagination;
}
