package com.teamgold.goldenharvest.common.infra.file.controller;

import com.teamgold.goldenharvest.common.infra.file.domain.dto.FileUploadResponse;
import com.teamgold.goldenharvest.common.infra.file.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping
    public ResponseEntity<FileUploadResponse> upload(
            @RequestParam("file") MultipartFile file) throws Exception {

        var saved = fileUploadService.upload(file);

        return ResponseEntity.ok(
                new FileUploadResponse(
                        saved.getFileId(),
                        saved.getFileUrl(),
                        saved.getContentType()
                )
        );
    }
}
