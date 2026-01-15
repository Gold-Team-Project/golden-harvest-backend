package com.teamgold.goldenharvest.domain.auth.command.application.service;

import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.LoginRequest;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.SignUpRequest;

public interface AuthService {
    void signup(SignUpRequest signUpRequest);


}
