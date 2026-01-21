package com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_inquiry_file")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long fileId;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private String originalName;

    private String uuidFilename;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private FileContentType contentType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public File(Long fileId,
                String fileUrl,
                String originalName,
                String uuidFilename,
                FileContentType contentType,
                LocalDateTime createdAt) {
        this.fileId = fileId;
        this.fileUrl = fileUrl;
        this.originalName = originalName;
        this.uuidFilename = uuidFilename;
        this.contentType = contentType;
        this.createdAt = createdAt;
    }
}
