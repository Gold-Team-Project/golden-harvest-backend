package com.teamgold.goldenharvest.domain.customersupport.command.application.event;
//user 쪽 event
public record InquiryCreatedEvent(
        String inquiryId,
        String userId
) {}
