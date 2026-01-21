package com.teamgold.goldenharvest.domain.auth.command.application.service;

import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.LoginRequest;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.SignUpRequest;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.response.TokenResponse;

public interface AuthService {
    void signup(SignUpRequest signUpRequest);

    TokenResponse login(LoginRequest loginRequest);

    TokenResponse reissue(String refreshToken);

    void logout(String accessToken, String email);

}
