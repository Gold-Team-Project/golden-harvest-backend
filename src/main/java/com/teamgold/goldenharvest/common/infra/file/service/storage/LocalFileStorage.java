package com.teamgold.goldenharvest.common.infra.file.service.storage;

import com.teamgold.goldenharvest.common.infra.file.domain.FileContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@Profile("local")
public class LocalFileStorage implements FileStorage {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public StoredFile store(MultipartFile file) throws IOException {

        String originalName = file.getOriginalFilename();
        String extension = "";

        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        String uuidName = UUID.randomUUID() + extension;

        Path target = Paths.get(uploadDir, uuidName);
        Files.createDirectories(target.getParent());
        Files.write(target, file.getBytes());

        return new StoredFile(
                "/files/" + uuidName,
                originalName,
                uuidName,
                FileContentType.from(file.getContentType())
        );
    }
}
