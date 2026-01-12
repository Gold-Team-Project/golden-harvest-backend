package com.teamgold.goldenharvest.domain.sales.command.application.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdatedEvent {
    private String userId;
    private String newEmail;
}