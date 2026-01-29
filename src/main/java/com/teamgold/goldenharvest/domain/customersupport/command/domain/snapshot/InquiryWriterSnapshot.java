package com.teamgold.goldenharvest.domain.customersupport.command.domain.snapshot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
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
	@Column(name = "user_email")
    private String userEmail;

	@Column(name = "company")
    private String company;

	@Column(name = "name")
    private String name;

	@Column(name = "phone_number")
    private String phoneNumber;

    @CreationTimestamp
    private LocalDateTime createdAt;

	@Builder
    public InquiryWriterSnapshot(
			String userEmail,
            String company,
            String name,
            String phoneNumber
    ) {
		this.userEmail = userEmail;
        this.company = company;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
