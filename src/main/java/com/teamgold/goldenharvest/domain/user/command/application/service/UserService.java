package com.teamgold.goldenharvest.domain.user.command.application.service;

import com.teamgold.goldenharvest.domain.user.command.application.dto.reponse.UserProfileResponse;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.UserProfileUpdateRequest;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.PasswordChangeRequest;

public interface UserService {

    void changePassword(String email, PasswordChangeRequest request);

    void updateProfile(String email, UserProfileUpdateRequest userProfileUpdateRequest);

    UserProfileResponse getUserProfile(String email);
}
