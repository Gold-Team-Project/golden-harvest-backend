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
        System.out.println("[Backend] 전체 회원 조회 API 호출됨!"); // 로그 추가
        List<UserAdminResponse> result = adminUserQueryService.getAllUsersForAdmin();
        System.out.println("[Backend] 조회된 데이터 개수: " + (result != null ? result.size() : 0));
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/update-requests")
    public ResponseEntity<ApiResponse<List<UserUpdateApprovalResponse>>> getPendingUpdateRequests() {
        // AdminUserQueryService에서 처리
        return ResponseEntity.ok(ApiResponse.success(adminUserQueryService.getPendingUpdateRequests()));
    }
}
