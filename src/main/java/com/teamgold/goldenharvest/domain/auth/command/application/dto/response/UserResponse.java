package com.teamgold.goldenharvest.domain.auth.command.application.dto.response;

import com.teamgold.goldenharvest.domain.user.command.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private String email;          // userId 역할
    private String name;           // 화면 표시용
    private String role;           // 권한 (ADMIN / USER)
    private String company;        // 회사명
    private String businessNumber; // 사업자번호
    private String phoneNumber;    // 전화번호
    private String addressLine1;   // 주소
    private String addressLine2;
    private String postalCode;
    private String fileUrl;        // 사업자 등록증
    private String status;         // ACTIVE / PENDING 등

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().getRoleStatusName())
                .company(user.getCompany())
                .businessNumber(user.getBusinessNumber())
                .phoneNumber(user.getPhoneNumber())
                .addressLine1(user.getAddressLine1())
                .addressLine2(user.getAddressLine2())
                .postalCode(user.getPostalCode())
                .fileUrl(user.getFileUrl())
                .status(user.getStatus().name())
                .build();
    }
}
