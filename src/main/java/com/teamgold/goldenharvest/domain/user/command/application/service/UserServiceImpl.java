package com.teamgold.goldenharvest.domain.user.command.application.service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.user.command.application.dto.reponse.UserProfileResponse;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.UserProfileUpdateRequest;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.PasswordChangeRequest;
import com.teamgold.goldenharvest.domain.user.command.domain.User;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override//  마이페이지 비밀번호 변경
    public void changePassword(String email, PasswordChangeRequest passwordChangeRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        //  현재 비밀번호가 맞는지 검증
        if (!passwordEncoder.matches(passwordChangeRequest.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        //  새 비밀번호가 기존 비밀번호와 동일한지 확인
        if (passwordEncoder.matches(passwordChangeRequest.getNewPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_SAME_AS_OLD);
        }

        //  새 비밀번호 암호화 및 업데이트
        user.updatePassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));

        // 비밀번호 변경 후 기존 리프레시 토큰 삭제 (모든 기기 로그아웃)
        redisTemplate.delete("RT:" + email);

        log.info("[Golden Harvest] 마이페이지 비밀번호 변경 완료: {}", email);

    }

    @Override
    public void updateProfile(String email, UserProfileUpdateRequest userProfileUpdateRequest) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.updateProfile(
                userProfileUpdateRequest.getName(),
                userProfileUpdateRequest.getPhoneNumber(),
                userProfileUpdateRequest.getAddressLine1(),
                userProfileUpdateRequest.getAddressLine2(),
                userProfileUpdateRequest.getPostalCode()
        );
    }

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
