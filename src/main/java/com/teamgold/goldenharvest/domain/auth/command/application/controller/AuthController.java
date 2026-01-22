package com.teamgold.goldenharvest.domain.auth.command.application.controller;

import com.teamgold.goldenharvest.common.mail.MailService;
import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.*;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.response.TokenResponse;
import com.teamgold.goldenharvest.domain.auth.command.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final MailService mailService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.signup(signUpRequest);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(tokenResponse));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenResponse>> reissue(@RequestHeader("Authorization") String refreshToken) {
        String token = refreshToken.startsWith("Bearer ") ? refreshToken.substring(7) : refreshToken;
        TokenResponse tokenResponse = authService.reissue(token);
        return ResponseEntity.ok(ApiResponse.success(tokenResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        authService.logout(accessToken, email);
        return ResponseEntity.ok(ApiResponse.success("로그아웃이 완료되었습니다."));
    }

    @PostMapping("/email/send")
    public ResponseEntity<ApiResponse<String>> sendEmail(@RequestBody @Valid EmailRequest emailRequest) {
        mailService.sendVerificationEmail(emailRequest.getEmail(), emailRequest.getType());
        return ResponseEntity.ok(ApiResponse.success("인증번호가 발송되었습니다."));
    }

    @PostMapping("/email/verify")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestBody @Valid VerificationRequest verificationRequest) {
        mailService.verifyCode(verificationRequest.getEmail(), verificationRequest.getCode());
        return ResponseEntity.ok(ApiResponse.success("이메일 인증에 성공하였습니다."));
    }
    //  비밀번호 재설정
    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @RequestBody @Valid PasswordResetRequest  passwordResetRequest) {

        authService.resetPassword(passwordResetRequest);

        return ResponseEntity.ok(ApiResponse.success("비밀번호가 성공적으로 재설정 되었습니다."));
    }
}
