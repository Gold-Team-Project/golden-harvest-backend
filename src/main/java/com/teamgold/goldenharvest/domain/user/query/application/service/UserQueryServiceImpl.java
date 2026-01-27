package com.teamgold.goldenharvest.domain.user.query.application.service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.user.command.domain.User;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import com.teamgold.goldenharvest.domain.user.query.application.dto.reponse.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return UserProfileResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .company(user.getCompany())
                .businessNumber(user.getBusinessNumber())
                .phoneNumber(user.getPhoneNumber())
                .addressLine1(user.getAddressLine1())
                .addressLine2(user.getAddressLine2())
                .postalCode(user.getPostalCode())
                .status(user.getStatus().name())
                .build();
    }
}
