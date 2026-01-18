package com.teamgold.goldenharvest.common.infra.file.domain.dto;


import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.FileContentType;

public record FileUploadResponse(
        Long fileId,
        String fileUrl,
        FileContentType contentType
) {}
