package com.teamgold.goldenharvest.domain.user.query.application.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserUpdateApprovalResponse {
    private Long id;
    private String userEmail;
    private String currentCompany;        // 비교를 위한 현재 회사명
    private String requestCompany;        // 바꾸고 싶은 회사명
    private String currentBusinessNumber; // 현재 사업자번호
    private String requestBusinessNumber; // 바꾸고 싶은 사업자번호
    private Long requestFileId;           // 증빙서류 ID
    private LocalDateTime createdAt;
}
