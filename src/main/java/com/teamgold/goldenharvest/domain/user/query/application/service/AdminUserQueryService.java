package com.teamgold.goldenharvest.domain.user.query.application.service;

import com.teamgold.goldenharvest.domain.user.query.application.dto.reponse.UserAdminResponse;
import com.teamgold.goldenharvest.domain.user.query.application.dto.reponse.UserUpdateApprovalResponse;

import java.util.List;

public interface AdminUserQueryService {

    List<UserAdminResponse> getAllUsersForAdmin();

    List<UserUpdateApprovalResponse> getPendingUpdateRequests();
}
