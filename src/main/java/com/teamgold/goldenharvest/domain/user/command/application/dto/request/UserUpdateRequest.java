package com.teamgold.goldenharvest.domain.user.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String requestCompany;  // 변경 희망 회사명
    private String requestBusinessNumber;   // 변경 희망 사업자번호
    private String requestFileUrl; // 새로 업로드한 증빙서류
}
