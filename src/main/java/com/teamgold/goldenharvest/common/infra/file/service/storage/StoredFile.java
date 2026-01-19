package com.teamgold.goldenharvest.common.infra.file.service.storage;

import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.FileContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoredFile {
    private String fileUrl;
    private String originalName;
    private String uuidFilename;
    private FileContentType contentType;
}
