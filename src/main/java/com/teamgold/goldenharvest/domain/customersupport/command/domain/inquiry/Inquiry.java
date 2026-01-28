package com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry;

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
    @Column
    private String inquiryId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String salesOrderId;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String body;

    private String comment;

    private Long fileId;

    @Enumerated(EnumType.STRING)
    private ProcessingStatus processingStatus = ProcessingStatus.N;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Inquiry(String inquiryId,
                   String userId,
                   String salesOrderId,
                   String title,
                   String body,
                   Long fileId,
                   String comment,
                   ProcessingStatus processingStatus,
                   LocalDateTime createdAt,
                   File file) {
        this.inquiryId = inquiryId;
        this.userId = userId;
        this.salesOrderId = salesOrderId;
        this.title = title;
        this.body = body;
        this.fileId = fileId;
        this.comment = comment;
        this.processingStatus = processingStatus;
        this.createdAt = createdAt;
    }

    //활성화 상태 변경
    public void updatedProcessingStatus(){
        this.processingStatus = ProcessingStatus.Y;
    }

    //문의 수정(제목, 내용)
    public void updatedInquiry(String title, String body){
        if (title != null) this.title = title;
        if (body !=null) this.body = body;
    }
    //문의 수정(제목, 내용)
    public void updatedComment(String comment){
        this.comment = comment;
    }

    public void updateFile(Long fileId) {
        this.fileId = fileId;
    }

}
