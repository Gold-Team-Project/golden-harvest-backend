package com.teamgold.goldenharvest.domain.user.command.application.event.dto;

import lombok.Builder;

@Builder
public record UserUpdatedEvent(
    String email,
    String company,
    String businessNumber,
    String name,
    String phoneNumber,
    String addressLine1,
    String addressLine2,
    String postalCode
) {}
