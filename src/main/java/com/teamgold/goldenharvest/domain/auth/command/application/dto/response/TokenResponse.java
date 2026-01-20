package com.teamgold.goldenharvest.domain.auth.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {

    private String accessToken;
    private String refreshToken;
}
