package com.teamgold.goldenharvest.domain.user.command.application.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdatedEvent {
    private String email;
    private String company;
    private String businessNumber;
    private String name;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
}
