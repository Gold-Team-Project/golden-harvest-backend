package com.teamgold.goldenharvest.domain.user.command.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "tb_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(name = "user_email", nullable = false)
    private String email; //이메일

    @Column(name = "user_password", nullable = false)
    private String password; //비밀번호

    @Column(name = "user_company", length = 20, nullable = false)
    private String company; //회사 이름

    @Column(length = 20)
    private String businessNumber; //사업자 번호

    @Column(name = "user_name", length = 20)
    private String name; //이름

    @Column(name = "user_phone", length = 20, nullable = false)
    private String phoneNumber; //전화번호

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserStatus status; //유저 상태

    @CreationTimestamp
    private LocalDateTime createdAt; //가입일

    @UpdateTimestamp
    private LocalDateTime updatedAt; //수정일

    @Column(length = 20)
    private String addressLine1; //도로명 주소

    @Column(length = 20)
    private String addressLine2; //상세 주소

    @Column(length = 20)
    private String postalCode; // 우편번호

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_status_id", nullable = false)
    private Role role;

    @Column(nullable = false, name = "file_url")
    private String fileUrl;

    @Builder
    public User(String email,
                String password,
                String company,
                String businessNumber,
                String name,
                String phoneNumber,
                UserStatus status,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,
                String addressLine1,
                String addressLine2,
                String postalCode,
                Role role,
                String fileUrl) {
        this.email = email;
        this.password = password;
        this.company = company;
        this.businessNumber = businessNumber;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postalCode = postalCode;
        this.role = role;
        this.fileUrl = fileUrl;
    }

    public void updatePassword(String encodePassword) {
        this.password = encodePassword;
    }

    public void updateProfile(String name, String phoneNumber,String addressLine1, String addressLine2, String postalCode) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postalCode = postalCode;
    }

    public void updateStatus(UserStatus Status) {
        this.status = Status;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public void updateBusinessInfo(String company, String businessNumber, String fileUrl) {
        this.company = company;
        this.businessNumber = businessNumber;
        this.fileUrl = fileUrl;
    }
}
