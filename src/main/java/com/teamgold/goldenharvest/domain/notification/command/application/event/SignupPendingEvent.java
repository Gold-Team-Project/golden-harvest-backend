package com.teamgold.goldenharvest.domain.notification.command.application.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignupPendingEvent {

    private final String userEmail;
}
