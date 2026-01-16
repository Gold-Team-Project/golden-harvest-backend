package com.teamgold.goldenharvest.domain.customersupport.command.domain.snapshot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_inquiry_writer_snapshot")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryWriterSnapshot {

    @Id
    @Column(name = "inquiry_id")
    private String inquiryId;

    private String userId;
    private String company;
    private String name;
    private String phoneNumber;
    private String email;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public InquiryWriterSnapshot(
            String inquiryId,
            String userId,
            String company,
            String name,
            String phoneNumber,
            String email
    ) {
        this.inquiryId = inquiryId;
        this.userId = userId;
        this.company = company;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
