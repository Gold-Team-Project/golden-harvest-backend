package com.teamgold.goldenharvest.common.infra.file.service;

import com.teamgold.goldenharvest.common.infra.file.domain.File;
import com.teamgold.goldenharvest.common.infra.file.infrastucture.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileDownloadService {

    private final FileRepository fileRepository;

    public ResponseEntity<Void> download(String fileUrl) {

        File file = fileRepository.findByFileUrl(fileUrl)
                .orElseThrow(() -> new RuntimeException("해당 경로의 파일을 찾을 수 없습니다."));

        return ResponseEntity
                .status(302)
                .header(HttpHeaders.LOCATION, file.getFileUrl())
                .build();
    }
}
