package com.teamgold.goldenharvest.domain.user.query.application.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserAdminResponse {
    private Long id;
    private String email;
    private String name;
    private String company;
    private String businessNumber;
    private String phoneNumber;
    private String status;
    private String role;
    private LocalDateTime createdAt;
}
