package com.teamgold.goldenharvest.domain.user.command.application.dto.request;

import com.teamgold.goldenharvest.domain.user.command.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserApproveRequest {
    private UserStatus userStatus;
}
