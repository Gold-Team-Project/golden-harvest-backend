package com.teamgold.goldenharvest.common.mail;


public record VerificationEmailRequestedEvent(
        String toEmail,
        String type,
        String code
) {}

