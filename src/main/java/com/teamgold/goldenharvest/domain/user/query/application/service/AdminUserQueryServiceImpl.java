package com.teamgold.goldenharvest.domain.user.query.application.service;

import com.teamgold.goldenharvest.domain.user.command.domain.RequestStatus;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserUpdateApprovalRepository;
import com.teamgold.goldenharvest.domain.user.query.application.dto.reponse.UserAdminResponse;
import com.teamgold.goldenharvest.domain.user.query.application.dto.reponse.UserUpdateApprovalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserQueryServiceImpl implements AdminUserQueryService {

    private final UserRepository userRepository;
    private final UserUpdateApprovalRepository userUpdateApprovalRepository;

    @Override
    public List<UserAdminResponse> getAllUsersForAdmin() {
        return userRepository.findAll().stream()
                .map(user -> UserAdminResponse.builder()
                        .email(user.getEmail())
                        .name(user.getName())
                        .company(user.getCompany())
                        .businessNumber(user.getBusinessNumber())
                        .phoneNumber(user.getPhoneNumber())
                        .status(user.getStatus().name())
                        .role(user.getRole().getRoleStatusName())
                        .createdAt(user.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
    @Override
    public List<UserUpdateApprovalResponse> getPendingUpdateRequests() {
        // 1. PENDING 상태인 것만 조회
        return userUpdateApprovalRepository.findByStatus(RequestStatus.PENDING).stream()
                .map(approval -> UserUpdateApprovalResponse.builder()
                        .id(approval.getId())
                        .userEmail(approval.getUser().getEmail())
                        .currentCompany(approval.getUser().getCompany()) // 기존 정보
                        .requestCompany(approval.getRequestCompany())   // 요청 정보
                        .currentBusinessNumber(approval.getUser().getBusinessNumber())
                        .requestBusinessNumber(approval.getRequestBusinessNumber())
                        .requestFileUrl(approval.getRequestFileUrl())
                        .createdAt(approval.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
