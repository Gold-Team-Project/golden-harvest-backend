package com.teamgold.goldenharvest.user;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.PasswordChangeRequest;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.UserProfileUpdateRequest;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.UserUpdateRequest;
import com.teamgold.goldenharvest.domain.user.command.application.service.UserServiceImpl;
import com.teamgold.goldenharvest.domain.user.command.domain.RequestStatus;
import com.teamgold.goldenharvest.domain.user.command.domain.User;
import com.teamgold.goldenharvest.domain.user.command.domain.UserUpdateApproval;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserUpdateApprovalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

// Mockito 관련 검증 메서드
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public @ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserUpdateApprovalRepository userUpdateApprovalRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("회원 정보 수정 성공 테스트")
    void updateProfile_Success() {
        // given
        String email = "test@example.com";
        User user = User.builder()
                .email(email)
                .name("이전이름")
                .phoneNumber("010-0000-0000")
                .build();

        UserProfileUpdateRequest request = UserProfileUpdateRequest.builder()
                .name("새이름")
                .phoneNumber("010-1234-5678")
                .addressLine1("서울시")
                .addressLine2("강남구")
                .postalCode("12345")
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        // when
        userService.updateProfile(email, request);

        // then
        assertThat(user.getName()).isEqualTo("새이름");
        assertThat(user.getPhoneNumber()).isEqualTo("010-1234-5678");
        assertThat(user.getAddressLine1()).isEqualTo("서울시");
    }

    @Test
    @DisplayName("이미 대기 중인 수정 요청이 있으면 예외가 발생한다")
    void requestBusinessUpdate_Fail_Duplicate() {
        // given
        String email = "test@example.com";
        User user = User.builder().email(email).build();
        UserUpdateRequest request = new UserUpdateRequest("회사", "123-45", 1L);

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        // 이미 PENDING 상태가 있다고 가정
        given(userUpdateApprovalRepository.existsByUserAndStatus(user, RequestStatus.PENDING)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.requestBusinessUpdate(email, request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_REQUEST);
    }
    @Test
    @DisplayName("비밀번호 변경 성공 - 기존 토큰 삭제 확인")
    void changePassword_Success() {
        // given
        String email = "test@example.com";
        String oldPw = "old1234";
        String newPw = "new1234";
        User user = User.builder().email(email).password("encodedOld").build();
        PasswordChangeRequest request = new PasswordChangeRequest(oldPw, newPw);

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(oldPw, "encodedOld")).willReturn(true); // 기존 비번 일치
        given(passwordEncoder.matches(newPw, "encodedOld")).willReturn(false); // 새 비번은 다름
        given(passwordEncoder.encode(newPw)).willReturn("encodedNew");

        // when
        userService.changePassword(email, request);

        // then
        assertThat(user.getPassword()).isEqualTo("encodedNew");
        verify(redisTemplate).delete("RT:" + email); // 리프레시 토큰 삭제 검증
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 현재 비밀번호 불일치")
    void changePassword_Fail_WrongOldPassword() {
        // given
        String email = "test@example.com";
        PasswordChangeRequest request = new PasswordChangeRequest("wrongOld", "new1234");
        User user = User.builder().email(email).password("encodedOld").build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrongOld", "encodedOld")).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.changePassword(email, request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PASSWORD_NOT_MATCH);
    }
    @Test
    @DisplayName("사업자 정보 수정 요청 성공 - 승인 대기 데이터 생성")
    void requestBusinessUpdate_Success() {
        // given
        String email = "test@example.com";
        User user = User.builder().email(email).build();
        UserUpdateRequest request = new UserUpdateRequest("새회사", "123-45", 1L);

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(userUpdateApprovalRepository.existsByUserAndStatus(user, RequestStatus.PENDING)).willReturn(false);

        // when
        userService.requestBusinessUpdate(email, request);

        // then
        verify(userUpdateApprovalRepository, times(1)).save(any(UserUpdateApproval.class));
    }
}