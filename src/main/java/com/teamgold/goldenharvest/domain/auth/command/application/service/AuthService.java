package com.teamgold.goldenharvest.domain.auth.command.application.service;

import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.LoginRequest;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.PasswordResetRequest;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.SignUpRequest;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.response.TokenResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AuthService {
    void signup(SignUpRequest signUpRequest, MultipartFile file);

    TokenResponse login(LoginRequest loginRequest);

    TokenResponse reissue(String refreshToken);

    void logout(String accessToken, String email);
    //  비밀번호 재설정(비밀번호 찾기)
    void resetPassword(PasswordResetRequest passwordResetRequest);

}
