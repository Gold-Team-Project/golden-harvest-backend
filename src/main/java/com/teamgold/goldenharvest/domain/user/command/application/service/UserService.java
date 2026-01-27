package com.teamgold.goldenharvest.domain.user.command.application.service;

import com.teamgold.goldenharvest.domain.user.command.application.dto.request.UserProfileUpdateRequest;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.PasswordChangeRequest;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.UserUpdateRequest;

public interface UserService {

    void changePassword(String email, PasswordChangeRequest request);

    void updateProfile(String email, UserProfileUpdateRequest userProfileUpdateRequest);

    void requestBusinessUpdate(String email, UserUpdateRequest userUpdateRequest);
}
