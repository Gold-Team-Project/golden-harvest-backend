package com.teamgold.goldenharvest.domain.user.command.application.controller;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.common.security.CustomUserDetails;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.UserProfileUpdateRequest;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.PasswordChangeRequest;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.UserUpdateRequest;
import com.teamgold.goldenharvest.domain.user.command.application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //  마이페이지 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails, // 현재 로그인된 사용자
            @RequestBody @Valid PasswordChangeRequest request) {

        userService.changePassword(userDetails.getEmail(), request);
        return ResponseEntity.ok(ApiResponse.success("비밀번호가 성공적으로 변경되었습니다."));
    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<String>> updateAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserProfileUpdateRequest userProfileUpdateRequest) {

        userService.updateProfile(userDetails.getUsername(), userProfileUpdateRequest);

        return ResponseEntity.ok(ApiResponse.success("회원 정보가 성공적으로 수정되었습니다."));
    }

    @PostMapping(value = "/business-update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> requestBusinessUpdate(
            @AuthenticationPrincipal CustomUserDetails userDetails, // 💡 타입을 CustomUserDetails로 변경
            @RequestPart("data") @Valid UserUpdateRequest userUpdateRequest,
            @RequestPart("file") MultipartFile file
    ) throws IOException {

        // CustomUserDetails의 @Getter 덕분에 getEmail()을 바로 사용할 수 있습니다.
        userService.requestBusinessUpdate(userDetails.getEmail(), userUpdateRequest, file);

        return ResponseEntity.ok(ApiResponse.success("사업자 정보 수정 요청이 성공적으로 접수되었습니다."));
    }
}