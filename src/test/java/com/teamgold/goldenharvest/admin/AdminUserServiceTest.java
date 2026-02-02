package com.teamgold.goldenharvest.admin;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.user.command.application.service.AdminUserCommandServiceImpl;
import com.teamgold.goldenharvest.domain.user.command.domain.*;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserUpdateApprovalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserUpdateApprovalRepository userUpdateApprovalRepository;

    @InjectMocks
    private AdminUserCommandServiceImpl adminService;

    @Test
    @DisplayName("신규 유저 승인 성공 - 상태가 PENDING에서 ACTIVE로 변경된다")
    void approveUser_Success() {
        // given
        String email = "farmer@test.com";
        User user = User.builder().email(email).status(UserStatus.PENDING).build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        // when
        adminService.approveUser(email, UserStatus.ACTIVE);

        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("정보 수정 승인 성공 - 유저 정보가 요청 데이터로 갱신된다")
    void approveProfileUpdate_Success() {
        // given
        Long approvalId = 1L;
        User user = User.builder()
                .email("user@test.com")
                .company("옛날농장")
                .businessNumber("000-00-00000")
                .build();

        UserUpdateApproval approval = UserUpdateApproval.builder()
                .user(user)
                .requestCompany("황금농장")
                .requestBusinessNumber("111-22-33333")
                .requestFileUrl("123")
                .status(RequestStatus.PENDING)
                .build();

        given(userUpdateApprovalRepository.findById(approvalId)).willReturn(Optional.of(approval));

        // when
        adminService.approveProfileUpdate(approvalId);

        // then
        assertThat(approval.getStatus()).isEqualTo(RequestStatus.APPROVED);
        assertThat(user.getCompany()).isEqualTo("황금농장");
        assertThat(user.getBusinessNumber()).isEqualTo("111-22-33333");
    }

    @Test
    @DisplayName("이미 승인된 건을 다시 승인 시도하면 예외가 발생한다")
    void approveProfileUpdate_Fail_AlreadyApproved() {
        // given
        Long approvalId = 1L;
        UserUpdateApproval approval = UserUpdateApproval.builder()
                .status(RequestStatus.APPROVED) // 이미 승인됨
                .build();
        given(userUpdateApprovalRepository.findById(approvalId)).willReturn(Optional.of(approval));

        // when & then
        assertThatThrownBy(() -> adminService.approveProfileUpdate(approvalId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_REQUEST);
    }
}