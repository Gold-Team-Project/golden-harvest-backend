package com.teamgold.goldenharvest.domain.user.command.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_user_update_approval")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdateApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", nullable = false)
    private User user;

    private String requestCompany;
    private String requestBusinessNumber;
    private String requestFileUrl;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public UserUpdateApproval(User user, String requestCompany, String requestBusinessNumber, Long requestFileId) {
        this.user = user;
        this.requestCompany = requestCompany;
        this.requestBusinessNumber = requestBusinessNumber;
        this.requestFileUrl= requestFileUrl;
        this.status = RequestStatus.PENDING;
    }

    // 승인 처리 메서드
    public void approve() {
        this.status = RequestStatus.APPROVED;
    }
}