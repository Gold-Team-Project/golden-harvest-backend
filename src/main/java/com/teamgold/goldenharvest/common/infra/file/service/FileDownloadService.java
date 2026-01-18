package com.teamgold.goldenharvest.common.infra.file.service;

import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.File;
import com.teamgold.goldenharvest.domain.customersupport.command.infrastructure.repository.inquiry.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileDownloadService {

    private final FileRepository fileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ResponseEntity<Resource> download(Long fileId) {

        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("파일 없음"));

        Path path = Paths.get(uploadDir, file.getUuidFilename());
        Resource resource = new FileSystemResource(path.toFile());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getOriginalName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
