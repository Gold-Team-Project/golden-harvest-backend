package com.teamgold.goldenharvest.domain.customersupport.command.application.event;

public record InquiryWriterResolvedEvent (
    String inquiryId,
    String userId,
    String company,
    String name,
    String phoneNumber,
    String email
){}
