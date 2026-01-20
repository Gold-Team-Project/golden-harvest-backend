package com.teamgold.goldenharvest.domain.notification.query.mapper;

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
      un.user_email           AS user_email,
      un.is_read              AS is_read,
      un.read_at              AS read_at,
      un.received_at          AS received_at,
      nt.type                 AS template_type,
      nt.title                AS template_title,
      nt.body                 AS template_body
    FROM tb_user_notification un
    JOIN tb_notification_template nt ON un.notification_type = nt.type
    WHERE un.user_email = #{userEmail}
    ORDER BY un.received_at DESC
    """)
    @Results(id = "UserNotificationDtoMap", value = {
            @Result(property = "userNotificationId", column = "user_notification_id", id = true),
            @Result(property = "userEmail",          column = "user_email"),
            @Result(property = "isRead",             column = "is_read"),
            @Result(property = "readAt",             column = "read_at"),
            @Result(property = "receivedAt",         column = "received_at"),

            // 중첩 객체 매핑 포인트
            @Result(property = "notificationTemplate.type",  column = "template_type"),
            @Result(property = "notificationTemplate.title", column = "template_title"),
            @Result(property = "notificationTemplate.body",  column = "template_body"),
    })
    List<UserNotificationDTO> selectUserNotifications(NotificationSearchRequest request);

    @Select("""
    SELECT
      un.user_notification_id AS user_notification_id,
      un.user_email           AS user_email,
      un.is_read              AS is_read,
      un.read_at              AS read_at,
      un.received_at          AS received_at,
      nt.type                 AS template_type,
      nt.title                AS template_title,
      nt.body                 AS template_body
    FROM tb_user_notification un
    JOIN tb_notification_template nt ON un.notification_type = nt.type
    ORDER BY un.received_at DESC
    LIMIT #{size} OFFSET #{offset}
    """)
    @Results(id = "UserNotificationDtoMap", value = {
            @Result(property = "userNotificationId", column = "user_notification_id", id = true),
            @Result(property = "userEmail",          column = "user_email"),
            @Result(property = "isRead",             column = "is_read"),
            @Result(property = "readAt",             column = "read_at"),
            @Result(property = "receivedAt",         column = "received_at"),
            @Result(property = "notificationTemplate.type",  column = "template_type"),
            @Result(property = "notificationTemplate.title", column = "template_title"),
            @Result(property = "notificationTemplate.body",  column = "template_body"),
    })
    List<UserNotificationDTO> selectAllNotifications(NotificationSearchRequest request);

    @Select("""
    SELECT COUNT(*)
    FROM tb_user_notification un
    WHERE un.user_email = #{userEmail}
    """)

    long countNotifications(NotificationSearchRequest request);

    @Select("""
    SELECT COUNT(*)
    FROM tb_user_notification un
    WHERE un.user_email = #{userEmail}
      AND un.is_read = 0
    """)

    Long countUnreadNotifications(String request);

    @Select("""
    SELECT COUNT(*)
    FROM tb_user_notification un
    JOIN tb_notification_template nt ON un.notification_type = nt.type
    """)
    long countAllNotifications();
}
