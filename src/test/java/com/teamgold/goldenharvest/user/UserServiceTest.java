package com.teamgold.goldenharvest.user;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.common.infra.file.domain.File;
import com.teamgold.goldenharvest.common.infra.file.service.FileUploadService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

// Mockito 관련 검증 메서드
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.willReturn;


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

    @Mock
    private FileUploadService fileUploadService;

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
    void requestBusinessUpdate_Success() throws IOException {
        // given
        String email = "test@example.com";
        User user = User.builder().email(email).build();
        UserUpdateRequest request = new UserUpdateRequest("새회사", "123-45", "https://example.com/file/test.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "business.jpg", "image/jpeg", "content".getBytes());

        File mockFile = org.mockito.Mockito.mock(File.class);

        given(mockFile.getFileUrl()).willReturn("https://example.com/file/test.jpg");
        given(fileUploadService.upload(file)).willReturn(mockFile);
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        // when
        userService.requestBusinessUpdate(email, request, file);

        // then
        verify(userUpdateApprovalRepository, times(1)).save(any(UserUpdateApproval.class));
    }
}