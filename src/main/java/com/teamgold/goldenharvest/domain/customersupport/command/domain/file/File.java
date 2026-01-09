package com.teamgold.goldenharvest.domain.customersupport.command.domain.file;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_file")
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
    @Column(length = 30, nullable = false)
    private FileContentType contentType;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
