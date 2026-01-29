package com.teamgold.goldenharvest.common.infra.file.service.storage;

import com.teamgold.goldenharvest.common.infra.file.domain.FileContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3FileStorage implements FileStorage {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.base-url}")
    private String baseUrl;

    @Override
    public StoredFile store(MultipartFile file) throws IOException {

        String originalName = file.getOriginalFilename();
        String extension = extractExtension(originalName);
        String storedName = UUID.randomUUID() + extension;

        String key = "uploads/" + storedName;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build(); //

        s3Client.putObject(
                request,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        return new StoredFile(
                baseUrl + storedName,
                originalName,
                storedName,
                FileContentType.from(file.getContentType())
        );
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
