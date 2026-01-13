package com.teamgold.goldenharvest.domain.sales.command.application.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatedEvent {
    private String userEmail;
    private String newUserEmail;
}