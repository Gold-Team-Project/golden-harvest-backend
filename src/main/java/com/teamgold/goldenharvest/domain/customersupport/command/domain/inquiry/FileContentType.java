package com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry;

import java.util.Arrays;

public enum FileContentType {

    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    APPLICATION_PDF("application/pdf"),
    APPLICATION_XLS("application/vnd.ms-excel"),
    APPLICATION_XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    TEXT_PLAIN("text/plain"),
    UNKNOWN("unknown");

    private final String mimeType;

    FileContentType(String mimeType) {
        this.mimeType = mimeType;
    }

    public static FileContentType from(String mimeType) {
        if (mimeType == null) return UNKNOWN;
        return Arrays.stream(values())
                .filter(v -> v.mimeType.equalsIgnoreCase(mimeType))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
