package com.teamgold.goldenharvest.auth;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.common.infra.file.service.FileUploadService;
import com.teamgold.goldenharvest.common.security.jwt.JwtProperties;
import com.teamgold.goldenharvest.common.security.jwt.JwtTokenProvider;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.LoginRequest;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.PasswordResetRequest;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.SignUpRequest;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.response.TokenResponse;
import com.teamgold.goldenharvest.domain.auth.command.application.service.AuthServiceImpl;
import com.teamgold.goldenharvest.domain.user.command.application.event.UserUpdatedEvent;
import com.teamgold.goldenharvest.domain.user.command.domain.Role; // Role 경로 확인 필요
import com.teamgold.goldenharvest.domain.user.command.domain.User;
import com.teamgold.goldenharvest.domain.user.command.domain.UserStatus;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.RoleRepository;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authServiceimpl;

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private FileUploadService fileUploadService;
    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private ValueOperations<String, Object> valueOperations;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private JwtProperties jwtProperties;
    @Mock private ApplicationEventPublisher eventPublisher;

    private final String email = "testuser@example.com";
    private final String password = "testPassword123!";

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("회원가입 성공 - PENDING 상태로 저장되어야 한다")
    void signup_success() throws IOException {
        SignUpRequest request = SignUpRequest.builder()
                .email(email)
                .password(password)
                .company("골든하베스트")
                .businessNumber("123")
                .name("리순신")
                .phoneNumber("010-1234-5678")

                .build();
        MockMultipartFile file = new MockMultipartFile("file", "b.jpg", "image/jpeg", "content".getBytes());

        given(userRepository.existsByEmail(email)).willReturn(false);
        given(valueOperations.get("EMAIL_VERIFIED:" + email)).willReturn("true");

        // File 클래스가 Inquiry 도메인에 있으므로 정확한 경로로 Mocking
        com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.File mockFile =
                mock(com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.File.class);
        given(mockFile.getFileId()).willReturn(50L);
        given(fileUploadService.upload(file)).willReturn(mockFile);

        Role role = Role.builder()
                .roleStatusId("ROLE_USER")
                .roleStatusName("일반유저")
                .roleStatusType("USER")
                .build();
        given(roleRepository.findById("ROLE_USER")).willReturn(Optional.of(role));
        given(passwordEncoder.encode(password)).willReturn("encoded_pw");

        authServiceimpl.signup(request, file);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(savedUser.getFileId()).isEqualTo(50L);
        verify(redisTemplate).delete("EMAIL_VERIFIED:" + email);

        verify(eventPublisher).publishEvent(any(UserUpdatedEvent.class));
    }

    @Test
    @DisplayName("로그인 실패 - 승인 대기(PENDING) 상태 유저는 예외가 발생한다")
    void login_fail_pending() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        User pendingUser = User.builder()
                .email(email)
                .password("encoded_pw")
                .status(UserStatus.PENDING)
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(pendingUser));
        given(passwordEncoder.matches(password, "encoded_pw")).willReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> authServiceimpl.login(loginRequest));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_APPROVED);
    }

    @Test
    @DisplayName("로그인 성공 - ACTIVE 유저는 토큰을 반환한다")
    void login_success() {
        LoginRequest loginRequest = new LoginRequest(email, password);
        User activeUser = User.builder()
                .email(email)
                .password("encoded_pw")
                .status(UserStatus.ACTIVE)
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(activeUser));
        given(passwordEncoder.matches(password, "encoded_pw")).willReturn(true);
        given(jwtTokenProvider.createAccessToken(activeUser)).willReturn("access-token");
        given(jwtTokenProvider.createRefreshToken(activeUser)).willReturn("refresh-token");
        given(jwtProperties.getRefreshTokenExpiration()).willReturn(3600000L); // 1시간

        TokenResponse response = authServiceimpl.login(loginRequest);

        assertThat(response.getAccessToken()).isEqualTo("access-token");
        // eq()와 any()를 사용하기 위해 ArgumentMatchers static import 필요
        verify(valueOperations).set(eq("RT:" + email), anyString(), any());
    }

    @Test
    @DisplayName("로그아웃 성공 - Redis에서 RT가 삭제되고 AT가 블랙리스트에 등록되어야 한다")
    void logout_success() {
        // given
        String accessToken = "test-access-token";
        String userEmail = "testuser@example.com";
        String redisKey = "RT:" + userEmail;
        Long remainingTime = 300000L; // 5분 남았다고 가정

        // Redis에 RT가 존재한다고 가정
        given(redisTemplate.hasKey(redisKey)).willReturn(true);
        // 토큰 만료 시간 계산 Mocking
        given(jwtTokenProvider.getExpiration(accessToken)).willReturn(remainingTime);
        // Redis opsForValue Mocking
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        // when
        authServiceimpl.logout(accessToken, userEmail);

        // then
        // 1. 리프레시 토큰 삭제 확인
        verify(redisTemplate, times(1)).delete(redisKey);

        // 2. 액세스 토큰 블랙리스트 등록 확인
        // "BL:토큰" 형태의 키로 남은 시간만큼 저장되었는지 확인
        verify(valueOperations, times(1)).set(
                eq("BL:" + accessToken),
                eq("logout"),
                eq(Duration.ofMillis(remainingTime))
        );
    }

    @Test
    @DisplayName("토큰 재발급 성공 - 유효한 RT인 경우 새로운 토큰 세트를 반환하고 Redis를 갱신한다")
    void reissue_success() {
        // given
        String oldRefreshToken = "old-refresh-token";
        String newAccessToken = "new-access-token";
        String newRefreshToken = "new-refresh-token";
        String userEmail = "testuser@example.com";

        // 1. 토큰 유효성 검사 통과 설정
        given(jwtTokenProvider.validateToken(oldRefreshToken)).willReturn(true);
        given(jwtTokenProvider.getEmailFromToken(oldRefreshToken)).willReturn(userEmail);

        // 2. Redis에 저장된 토큰과 일치하는지 확인 설정
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("RT:" + userEmail)).willReturn(oldRefreshToken);

        // 3. 새로운 토큰 발급 및 유저 조회 설정
        User user = User.builder().email(userEmail).build();
        given(userRepository.findByEmail(userEmail)).willReturn(Optional.of(user));
        given(jwtTokenProvider.createAccessToken(user)).willReturn(newAccessToken);
        given(jwtTokenProvider.createRefreshToken(user)).willReturn(newRefreshToken);
        given(jwtProperties.getRefreshTokenExpiration()).willReturn(3600000L);

        // when
        TokenResponse response = authServiceimpl.reissue(oldRefreshToken);

        // then
        // 결과 확인
        assertThat(response.getAccessToken()).isEqualTo(newAccessToken);
        assertThat(response.getRefreshToken()).isEqualTo(newRefreshToken);

        // Redis 갱신 확인 (새로운 RT로 교체되었는지)
        verify(valueOperations).set(
                eq("RT:" + userEmail),
                eq(newRefreshToken),
                any(Duration.class)
        );
    }

    @Test
    @DisplayName("토큰 재발급 실패 - Redis에 저장된 토큰과 일치하지 않으면 예외가 발생한다")
    void reissue_fail_mismatch() {
        // given
        String clientToken = "wrong-token";
        String serverToken = "correct-token";
        String userEmail = "testuser@example.com";

        given(jwtTokenProvider.validateToken(clientToken)).willReturn(true);
        given(jwtTokenProvider.getEmailFromToken(clientToken)).willReturn(userEmail);

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("RT:" + userEmail)).willReturn(serverToken); // 불일치 발생

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> authServiceimpl.reissue(clientToken));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.REFRESH_TOKEN_MISMATCH);
    }

    @Test
    @DisplayName("비밀번호 재설정 성공 - 새 비밀번호가 암호화되어 저장되고 인증 기록과 RT가 삭제되어야 한다")
    void resetPassword_success() {
        // given
        String newPassword = "newPassword123!";
        PasswordResetRequest request = new PasswordResetRequest(email, newPassword);

        // 1. Redis 이메일 인증 완료 상태 설정
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("EMAIL_VERIFIED:" + email)).willReturn("true");

        // 2. 유저 조회 설정
        User user = spy(User.builder().email(email).password("oldPassword").build());
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        // 3. 비밀번호 암호화 설정
        given(passwordEncoder.encode(newPassword)).willReturn("encodedNewPassword");

        // when
        authServiceimpl.resetPassword(request);

        // then
        // 비밀번호 업데이트 확인 (엔티티의 updatePassword 메서드 호출 여부)
        verify(user).updatePassword("encodedNewPassword");

        // 인증 기록 및 리프레시 토큰 삭제 확인
        verify(redisTemplate).delete("EMAIL_VERIFIED:" + email);
        verify(redisTemplate).delete("RT:" + email);
    }

    @Test
    @DisplayName("비밀번호 재설정 실패 - 이메일 인증이 되지 않은 경우 예외가 발생한다")
    void resetPassword_fail_not_verified() {
        // given
        PasswordResetRequest request = new PasswordResetRequest(email, "newPassword123!");

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("EMAIL_VERIFIED:" + email)).willReturn(null); // 인증 안됨

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> authServiceimpl.resetPassword(request));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.EMAIL_VERIFICATION_REQUIRED);
    }
}