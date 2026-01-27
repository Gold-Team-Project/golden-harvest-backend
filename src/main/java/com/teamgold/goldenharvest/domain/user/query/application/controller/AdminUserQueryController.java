package com.teamgold.goldenharvest.domain.user.query.application.controller;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.user.query.application.dto.reponse.UserAdminResponse;
import com.teamgold.goldenharvest.domain.user.query.application.dto.reponse.UserUpdateApprovalResponse;
import com.teamgold.goldenharvest.domain.user.query.application.service.AdminUserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class AdminUserQueryController {
    private final AdminUserQueryService adminUserQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserAdminResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(adminUserQueryService.getAllUsersForAdmin()));
    }

    @GetMapping("/update-requests")
    public ResponseEntity<ApiResponse<List<UserUpdateApprovalResponse>>> getPendingUpdateRequests() {
        // AdminUserQueryService에서 처리
        return ResponseEntity.ok(ApiResponse.success(adminUserQueryService.getPendingUpdateRequests()));
    }
}
