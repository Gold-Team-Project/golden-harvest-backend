package com.teamgold.goldenharvest.domain.auth.command.application.service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.LoginRequest;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.SignUpRequest;
import com.teamgold.goldenharvest.domain.user.command.application.event.UserUpdatedEvent;
import com.teamgold.goldenharvest.domain.user.command.domain.Role;
import com.teamgold.goldenharvest.domain.user.command.domain.User;
import com.teamgold.goldenharvest.domain.user.command.domain.UserStatus;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.RoleRepository;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // User 정보 이벤트 리스너 (정동욱)
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void signup(SignUpRequest signUpRequest) {   // 이메일 중복 여부 확인
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Role role = roleRepository.findById("ROLE_USER")    // 사용자 권한 조회
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());   // 비밀번호 암호화

        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(encodedPassword)
                .company(signUpRequest.getCompany())
                .businessNumber(signUpRequest.getBusinessNumber())
                .name(signUpRequest.getName())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .status(UserStatus.ACTIVE)
                .role(role)
                .fileId(signUpRequest.getFileId())
                .build();

        userRepository.save(user);

        // 이벤트 발행 시 요청되는 값 (정동욱)
        UserUpdatedEvent event = UserUpdatedEvent.builder()
                .email(user.getEmail())
                .company(user.getCompany())
                .businessNumber(user.getBusinessNumber())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .addressLine1(user.getAddressLine1())
                .addressLine2(user.getAddressLine2())
                .postalCode(user.getPostalCode())
                .build();
        eventPublisher.publishEvent(event);
    }
    
}
