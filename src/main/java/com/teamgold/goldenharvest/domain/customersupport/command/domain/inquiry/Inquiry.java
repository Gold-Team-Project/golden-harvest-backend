package com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry;

import com.teamgold.goldenharvest.domain.customersupport.command.domain.file.File;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_inquiry")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry {

    @Id
    @Column(length = 20)
    private String inquiryId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String salesOrderId;

    private String title;

    private String body;

    private String comment;

    @Enumerated(EnumType.STRING)
    private ProcessingStatus processingStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    @Builder
    public Inquiry(String inquiryId,
                   String userId,
                   String salesOrderId,
                   String title,
                   String body,
                   String comment,
                   ProcessingStatus processingStatus,
                   LocalDateTime createdAt,
                   File file) {
        this.inquiryId = inquiryId;
        this.userId = userId;
        this.salesOrderId = salesOrderId;
        this.title = title;
        this.body = body;
        this.comment = comment;
        this.processingStatus = processingStatus;
        this.createdAt = createdAt;
        this.file = file;
    }
}
