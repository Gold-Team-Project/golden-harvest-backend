package com.teamgold.goldenharvest.domain.user.query.application.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserProfileResponse {
    private String email;
    private String name;
    private String company;
    private String businessNumber;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private String status;
}
