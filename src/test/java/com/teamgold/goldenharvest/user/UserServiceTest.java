package com.teamgold.goldenharvest.user;

import com.teamgold.goldenharvest.domain.user.command.application.dto.request.UserProfileUpdateRequest;
import com.teamgold.goldenharvest.domain.user.command.application.service.UserServiceImpl;
import com.teamgold.goldenharvest.domain.user.command.domain.User;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


public @ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

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
}