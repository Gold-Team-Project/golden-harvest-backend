package com.teamgold.goldenharvest.domain.notification.query.mapper;

import com.teamgold.goldenharvest.domain.notification.command.domain.aggregate.UserNotification;
import com.teamgold.goldenharvest.domain.notification.query.dto.request.NotificationSearchRequest;
import com.teamgold.goldenharvest.domain.notification.query.dto.response.UserNotificationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotificationMapper {

    @Select("""
    SELECT
      un.user_notification_id AS user_notification_id,
      u.email                 AS user_email,
      un.is_read              AS is_read,
      un.read_at              AS read_at,
      un.received_at          AS received_at,
      nt.type                 AS template_type,
      nt.title                AS template_title,
      nt.body                 AS template_body
    FROM tb_user_notification un
    JOIN tb_notification_template nt ON un.notification_template_id = nt.notification_template_id
    JOIN tb_user u ON un.user_id = u.user_id
    WHERE un.user_id = #{userId}
    ORDER BY un.received_at DESC
    """)
    @Results(id="UserNotificationMap", value = {
            @Result(property="userNotificationId", column="user_notification_id"),
            @Result(property="userEmail",          column="user_email"),
            @Result(property="isRead",              column="is_read"),
            @Result(property="readAt",            column="read_at"),
            @Result(property="receivedAt",        column="received_at"),
            @Result(property="notificationTemplate.type",  column="template_type"),
            @Result(property="notificationTemplate.title", column="template_title"),
            @Result(property="notificationTemplate.body",  column="template_body")
    })

    List<UserNotificationDTO> selectUserNotifications(NotificationSearchRequest request);

    long countNotifications(NotificationSearchRequest request);

}
