package com.teamgold.goldenharvest.admin;

import com.teamgold.goldenharvest.domain.user.command.domain.*;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserUpdateApprovalRepository;
import com.teamgold.goldenharvest.domain.user.query.application.dto.reponse.UserUpdateApprovalResponse;
import com.teamgold.goldenharvest.domain.user.query.application.service.AdminUserQueryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AdminUserQueryServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserUpdateApprovalRepository userUpdateApprovalRepository;

    @InjectMocks
    private AdminUserQueryServiceImpl adminQueryService;

    @Test
    @DisplayName("대기 중인 수정 요청 목록을 정확히 조회한다")
    void getPendingUpdateRequests_Success() {
        // given
        User user = User.builder().email("test@test.com").company("기존회사").build();
        UserUpdateApproval approval = UserUpdateApproval.builder()
                .user(user)
                .requestCompany("요청회사")
                .status(RequestStatus.PENDING)
                .build();

        given(userUpdateApprovalRepository.findByStatus(RequestStatus.PENDING))
                .willReturn(List.of(approval));

        // when
        List<UserUpdateApprovalResponse> result = adminQueryService.getPendingUpdateRequests();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserEmail()).isEqualTo("test@test.com");
        assertThat(result.get(0).getRequestCompany()).isEqualTo("요청회사");
        assertThat(result.get(0).getCurrentCompany()).isEqualTo("기존회사");
    }
}