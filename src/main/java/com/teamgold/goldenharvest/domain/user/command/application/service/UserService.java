package com.teamgold.goldenharvest.domain.user.command.application.service;

import com.teamgold.goldenharvest.domain.user.command.application.dto.request.PasswordChangeRequest;
import com.teamgold.goldenharvest.domain.user.command.domain.User;

public interface UserService {

    void changePassword(String email, PasswordChangeRequest request);
}
