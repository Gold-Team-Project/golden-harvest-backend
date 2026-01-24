package com.teamgold.goldenharvest.domain.user.query.application.service;

import com.teamgold.goldenharvest.domain.user.query.application.dto.reponse.UserProfileResponse;

public interface UserQueryService {

    UserProfileResponse getUserProfile(String email);
}
