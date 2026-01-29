package com.teamgold.goldenharvest.notification;

import com.teamgold.goldenharvest.domain.notification.query.dto.request.NotificationSearchRequest;
import com.teamgold.goldenharvest.domain.notification.query.dto.response.NotificationListResponse;
import com.teamgold.goldenharvest.domain.notification.query.dto.response.UserNotificationDTO;
import com.teamgold.goldenharvest.domain.notification.query.mapper.NotificationMapper;
import com.teamgold.goldenharvest.domain.notification.query.service.NotificationQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class NotificationQueryServiceTest {

    @InjectMocks
    private NotificationQueryService notificationQueryService;

    @Mock
    private NotificationMapper notificationMapper;

    @Test
    void getNotificationList_returnsNotificationListWithPagination() {
        // given
        NotificationSearchRequest request = NotificationSearchRequest.builder()
                .userEmail("test@teamgold.com")
                .page(1)
                .size(10)
                .build();

        UserNotificationDTO dto1 = UserNotificationDTO.builder()
                .userNotificationId(1L)
                .userEmail("test@teamgold.com")
                .isRead(false)
                .build();

        UserNotificationDTO dto2 = UserNotificationDTO.builder()
                .userNotificationId(2L)
                .userEmail("test@teamgold.com")
                .isRead(true)
                .build();

        List<UserNotificationDTO> notifications = Arrays.asList(dto1, dto2);

        given(notificationMapper.selectUserNotifications(request)).willReturn(notifications);
        given(notificationMapper.countNotifications(request)).willReturn(25L);

        // when
        NotificationListResponse response = notificationQueryService.getNotificationList(request);

        // then
        then(notificationMapper).should(times(1)).selectUserNotifications(request);
        then(notificationMapper).should(times(1)).countNotifications(request);

        assertThat(response.getNotifications()).hasSize(2);
        assertThat(response.getNotifications()).containsExactly(dto1, dto2);
        assertThat(response.getPagination().getCurrentPage()).isEqualTo(1);
        assertThat(response.getPagination().getTotalPages()).isEqualTo(3);
        assertThat(response.getPagination().getTotalItems()).isEqualTo(25L);
    }

    @Test
    void getAllNotifications_returnsAllNotificationsWithPagination() {
        // given
        NotificationSearchRequest request = NotificationSearchRequest.builder()
                .page(1)
                .size(20)
                .build();

        UserNotificationDTO dto1 = UserNotificationDTO.builder()
                .userNotificationId(1L)
                .userEmail("user1@teamgold.com")
                .isRead(false)
                .build();

        UserNotificationDTO dto2 = UserNotificationDTO.builder()
                .userNotificationId(2L)
                .userEmail("user2@teamgold.com")
                .isRead(false)
                .build();

        List<UserNotificationDTO> notifications = Arrays.asList(dto1, dto2);

        given(notificationMapper.selectUserNotifications(request)).willReturn(notifications);
        given(notificationMapper.countAllNotifications()).willReturn(100L);

        // when
        NotificationListResponse response = notificationQueryService.getAllNotifications(request);

        // then
        then(notificationMapper).should(times(1)).selectUserNotifications(request);
        then(notificationMapper).should(times(1)).countAllNotifications();

        assertThat(response.getNotifications()).hasSize(2);
        assertThat(response.getNotifications()).containsExactly(dto1, dto2);
        assertThat(response.getPagination().getCurrentPage()).isEqualTo(1);
        assertThat(response.getPagination().getTotalPages()).isEqualTo(5);
        assertThat(response.getPagination().getTotalItems()).isEqualTo(100L);
    }

    @Test
    void countUnreadNotifications_returnsUnreadCount() {
        // given
        String userEmail = "test@teamgold.com";
        given(notificationMapper.countUnreadNotifications(userEmail)).willReturn(5L);

        // when
        Long unreadCount = notificationQueryService.countUnreadNotifications(userEmail);

        // then
        then(notificationMapper).should(times(1)).countUnreadNotifications(userEmail);
        assertThat(unreadCount).isEqualTo(5L);
    }

    @Test
    void getNotificationList_handlesEmptyList() {
        // given
        NotificationSearchRequest request = NotificationSearchRequest.builder()
                .userEmail("test@teamgold.com")
                .page(1)
                .size(10)
                .build();

        given(notificationMapper.selectUserNotifications(request)).willReturn(List.of());
        given(notificationMapper.countNotifications(request)).willReturn(0L);

        // when
        NotificationListResponse response = notificationQueryService.getNotificationList(request);

        // then
        assertThat(response.getNotifications()).isEmpty();
        assertThat(response.getPagination().getCurrentPage()).isEqualTo(1);
        assertThat(response.getPagination().getTotalPages()).isEqualTo(0);
        assertThat(response.getPagination().getTotalItems()).isEqualTo(0L);
    }

    @Test
    void getNotificationList_calculatesPaginationCorrectly() {
        // given
        NotificationSearchRequest request = NotificationSearchRequest.builder()
                .userEmail("test@teamgold.com")
                .page(2)
                .size(15)
                .build();

        given(notificationMapper.selectUserNotifications(request)).willReturn(List.of());
        given(notificationMapper.countNotifications(request)).willReturn(32L);

        // when
        NotificationListResponse response = notificationQueryService.getNotificationList(request);

        // then
        assertThat(response.getPagination().getCurrentPage()).isEqualTo(2);
        assertThat(response.getPagination().getTotalPages()).isEqualTo(3);
        assertThat(response.getPagination().getTotalItems()).isEqualTo(32L);
    }
}
