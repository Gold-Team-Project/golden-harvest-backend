package com.teamgold.goldenharvest.domain.user.command.application.controller;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.UserApproveRequest;
import com.teamgold.goldenharvest.domain.user.command.application.service.AdminUserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
