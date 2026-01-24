package com.teamgold.goldenharvest.user;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.user.query.application.dto.reponse.UserProfileResponse;
import com.teamgold.goldenharvest.domain.user.command.domain.User;
import com.teamgold.goldenharvest.domain.user.command.domain.UserStatus;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import com.teamgold.goldenharvest.domain.user.query.application.service.UserQueryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public @ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserQueryServiceImpl userQueryService;

    @Test
    @DisplayName("로그인한 유저의 이메일로 프로필 정보를 정확히 조회한다")
    void getUserProfile_Success() {
        // given
        String email = "gold@harvest.com";
        User user = User.builder()
                .email(email)
                .name("김농부")
                .company("황금농장")
                .phoneNumber("010-1111-2222")
                .status(UserStatus.ACTIVE)
                .addressLine1("경기도 부천시")
                .postalCode("12345")
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        // when
        UserProfileResponse response = userQueryService.getUserProfile(email);

        // then
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getName()).isEqualTo("김농부");
        assertThat(response.getCompany()).isEqualTo("황금농장");
        assertThat(response.getAddressLine1()).isEqualTo("경기도 부천시");

        // 조회의 핵심: 필드 누락이 없는지 검증
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 프로필 조회 시 예외 발생")
    void getUserProfile_Fail_UserNotFound() {
        // given
        String email = "none@test.com";
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userQueryService.getUserProfile(email))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);
    }
}