package com.teamgold.goldenharvest.domain.user.command.application.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequest {
    private String name;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
}
