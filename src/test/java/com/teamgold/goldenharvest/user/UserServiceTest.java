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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserUpdateApprovalRepository userUpdateApprovalRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    RedisTemplate<String, Object> redisTemplate;

    @Mock
    FileUploadService fileUploadService;

    @InjectMocks
    UserServiceImpl userService;

    // ===============================
    // 프로필 수정
    // ===============================
    @Test
    @DisplayName("회원 정보 수정 성공")
    void updateProfile_Success() {

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

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        userService.updateProfile(email, request);

        assertThat(user.getName()).isEqualTo("새이름");
        assertThat(user.getPhoneNumber()).isEqualTo("010-1234-5678");
        assertThat(user.getAddressLine1()).isEqualTo("서울시");
        assertThat(user.getAddressLine2()).isEqualTo("강남구");
        assertThat(user.getPostalCode()).isEqualTo("12345");

        verify(userRepository).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

    // ===============================
    // 사업자 정보 수정 요청
    // ===============================
    @Test
    @DisplayName("사업자 정보 수정 요청 성공 (파일 업로드 후 fileUrl 저장)")
    void requestBusinessUpdate_Success() throws Exception {

        String email = "test@example.com";
        User user = User.builder().email(email).build();

        // DTO에 fileId 같은 게 있어도 서비스에서는 MultipartFile 업로드 결과(fileUrl)를 저장함
        UserUpdateRequest request = new UserUpdateRequest("새회사", "123-45","https://s3.aws.com/business.png" );

        MultipartFile file = mock(MultipartFile.class);

        File uploadedFile = File.builder()
                .fileUrl("https://s3.aws.com/business.png")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(fileUploadService.upload(file)).thenReturn(uploadedFile);

        userService.requestBusinessUpdate(email, request, file);

        ArgumentCaptor<UserUpdateApproval> captor = ArgumentCaptor.forClass(UserUpdateApproval.class);
        verify(userUpdateApprovalRepository).save(captor.capture());

        UserUpdateApproval saved = captor.getValue();
        assertThat(saved.getUser()).isEqualTo(user);
        assertThat(saved.getRequestCompany()).isEqualTo("새회사");
        assertThat(saved.getRequestBusinessNumber()).isEqualTo("123-45");
        assertThat(saved.getRequestFileUrl()).isEqualTo("https://s3.aws.com/business.png");
        assertThat(saved.getStatus()).isEqualTo(RequestStatus.PENDING);

        verify(userRepository).findByEmail(email);
        verify(fileUploadService).upload(file);
        verify(userUpdateApprovalRepository).save(any(UserUpdateApproval.class));
        verifyNoMoreInteractions(userRepository, fileUploadService, userUpdateApprovalRepository);
    }

    @Test
    @DisplayName("사업자 정보 수정 요청 실패 - 파일 업로드 IOException -> FILE_UPLOAD_ERROR")
    void requestBusinessUpdate_Fail_FileUploadIOException() throws Exception {

        String email = "test@example.com";
        User user = User.builder().email(email).build();

        UserUpdateRequest request = new UserUpdateRequest("회사", "123-45", "https://s3.aws.com/business.png");
        MultipartFile file = mock(MultipartFile.class);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(fileUploadService.upload(file)).thenThrow(new IOException("S3 error"));

        assertThatThrownBy(() -> userService.requestBusinessUpdate(email, request, file))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FILE_UPLOAD_ERROR);

        verify(userRepository).findByEmail(email);
        verify(fileUploadService).upload(file);
        verify(userUpdateApprovalRepository, never()).save(any());
    }

    // ===============================
    // 비밀번호 변경
    // ===============================
    @Test
    @DisplayName("비밀번호 변경 성공 (리프레시 토큰 삭제)")
    void changePassword_Success() {

        String email = "test@example.com";

        User user = User.builder()
                .email(email)
                .password("encodedOld")
                .build();

        PasswordChangeRequest request = new PasswordChangeRequest("old1234", "new1234");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old1234", "encodedOld")).thenReturn(true);
        when(passwordEncoder.matches("new1234", "encodedOld")).thenReturn(false);
        when(passwordEncoder.encode("new1234")).thenReturn("encodedNew");

        userService.changePassword(email, request);

        assertThat(user.getPassword()).isEqualTo("encodedNew");
        verify(redisTemplate).delete("RT:" + email);

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches("old1234", "encodedOld");
        verify(passwordEncoder).matches("new1234", "encodedOld");
        verify(passwordEncoder).encode("new1234");
        verify(redisTemplate).delete("RT:" + email);
        verifyNoMoreInteractions(userRepository, passwordEncoder, redisTemplate);
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 현재 비밀번호 불일치")
    void changePassword_Fail_WrongOldPassword() {

        String email = "test@example.com";

        User user = User.builder()
                .email(email)
                .password("encodedOld")
                .build();

        PasswordChangeRequest request = new PasswordChangeRequest("wrongOld", "new1234");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongOld", "encodedOld")).thenReturn(false);

        assertThatThrownBy(() -> userService.changePassword(email, request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PASSWORD_NOT_MATCH);

        verify(redisTemplate, never()).delete(anyString());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 새 비밀번호가 기존과 동일")
    void changePassword_Fail_SameAsOld() {

        String email = "test@example.com";

        User user = User.builder()
                .email(email)
                .password("encodedOld")
                .build();

        PasswordChangeRequest request = new PasswordChangeRequest("old1234", "old1234");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old1234", "encodedOld")).thenReturn(true);
        // 새 비밀번호도 기존 해시와 매칭되면 동일로 판단
        when(passwordEncoder.matches("old1234", "encodedOld")).thenReturn(true);

        assertThatThrownBy(() -> userService.changePassword(email, request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PASSWORD_SAME_AS_OLD);

        verify(redisTemplate, never()).delete(anyString());
        verify(passwordEncoder, never()).encode(anyString());
    }
}
