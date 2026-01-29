package com.teamgold.goldenharvest.common.infra.file.controller;

import com.teamgold.goldenharvest.common.infra.file.service.FileDownloadService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileDownloadController {

    private final FileDownloadService fileDownloadService;

    @GetMapping("/download")
    public ResponseEntity<?> download(@RequestParam("url") String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty() || fileUrl.equals("-0")) {
            return ResponseEntity.badRequest().build();
        }
        return fileDownloadService.download(fileUrl);
    }
}
