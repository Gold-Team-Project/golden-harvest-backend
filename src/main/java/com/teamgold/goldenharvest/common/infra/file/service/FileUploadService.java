package com.teamgold.goldenharvest.common.infra.file.service;

import com.teamgold.goldenharvest.common.infra.file.domain.File;
import com.teamgold.goldenharvest.common.infra.file.repository.FileRepository;
import com.teamgold.goldenharvest.common.infra.file.service.storage.FileStorage;
import com.teamgold.goldenharvest.common.infra.file.service.storage.StoredFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final FileStorage fileStorage;
    private final FileRepository fileRepository;

    public File upload(MultipartFile file) throws IOException {

        StoredFile stored = fileStorage.store(file);

        File entity = File.builder()
                .fileUrl(stored.getFileUrl())
                .originalName(stored.getOriginalName())
                .uuidFilename(stored.getUuidFilename())
                .contentType(stored.getContentType())
                .createdAt(LocalDateTime.now())
                .build();

        return fileRepository.save(entity);
    }
}
