package com.teamgold.goldenharvest.domain.user.command.application.service;

import com.teamgold.goldenharvest.domain.user.command.domain.UserStatus;

public interface AdminUserCommandService {

    void approveUser(String email, UserStatus newStatus);

    void approveProfileUpdate(Long requestId);

	void publishAllUserDetailsEvent();
}
