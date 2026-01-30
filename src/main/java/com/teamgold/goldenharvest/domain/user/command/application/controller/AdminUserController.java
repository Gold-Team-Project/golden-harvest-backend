package com.teamgold.goldenharvest.domain.user.command.application.controller;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.common.security.CustomUserDetails;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.UserApproveRequest;
import com.teamgold.goldenharvest.domain.user.command.application.service.AdminUserCommandService;
import com.teamgold.goldenharvest.domain.user.command.domain.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserCommandService adminUserCommandService;

    // 신규 가입 승인
    @PatchMapping("/{email}/approve")
    public ResponseEntity<ApiResponse<Void>> approveUser(
            @PathVariable String email,
            @RequestBody UserApproveRequest request) {

        adminUserCommandService.approveUser(email, request.getUserStatus());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    // 정보 수정 승인
    @PatchMapping("/update-requests/{requestId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveProfileUpdate(@PathVariable Long requestId) {
        adminUserCommandService.approveProfileUpdate(requestId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/{targetEmail}/status")
    public ResponseEntity<ApiResponse<Void>> updateUserStatus(
            @AuthenticationPrincipal CustomUserDetails adminDetails, // 본인 확인용
            @PathVariable String targetEmail,
            @RequestParam UserStatus newStatus) {

        adminUserCommandService.updateUserStatus(targetEmail, newStatus, adminDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/{targetEmail}/role")
    public ResponseEntity<ApiResponse<Void>> updateUserRole(
            @AuthenticationPrincipal CustomUserDetails adminDetails,
            @PathVariable String targetEmail,
            @RequestParam String newRole) { // 또는 프로젝트의 Role Enum 타입 사용

        // adminUserCommandService에도 해당 기능을 수행하는 메서드를 만들어야 합니다.
        adminUserCommandService.updateUserRole(targetEmail, newRole, adminDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

	@PostMapping("/publish/all")
	public ResponseEntity<ApiResponse<?>> publishUserDetails() {
		adminUserCommandService.publishAllUserDetailsEvent();
		return ResponseEntity.ok(ApiResponse.success(null));
	}
}
