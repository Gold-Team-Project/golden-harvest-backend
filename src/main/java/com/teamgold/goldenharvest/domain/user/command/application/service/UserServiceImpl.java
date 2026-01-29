package com.teamgold.goldenharvest.domain.user.command.application.service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.common.infra.file.service.FileUploadService;
import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.File;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.UserProfileUpdateRequest;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.PasswordChangeRequest;
import com.teamgold.goldenharvest.domain.user.command.application.dto.request.UserUpdateRequest;
import com.teamgold.goldenharvest.domain.user.command.domain.RequestStatus;
import com.teamgold.goldenharvest.domain.user.command.domain.User;
import com.teamgold.goldenharvest.domain.user.command.domain.UserUpdateApproval;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserUpdateApprovalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserUpdateApprovalRepository userUpdateApprovalRepository;
    private final FileUploadService fileUploadService;

    @Override//  마이페이지 비밀번호 변경
    public void changePassword(String email, PasswordChangeRequest passwordChangeRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

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
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.updateProfile(
                userProfileUpdateRequest.getName(),
                userProfileUpdateRequest.getPhoneNumber(),
                userProfileUpdateRequest.getAddressLine1(),
                userProfileUpdateRequest.getAddressLine2(),
                userProfileUpdateRequest.getPostalCode()
        );
    }

    @Override
    @Transactional
    public void requestBusinessUpdate(String email, UserUpdateRequest userUpdateRequest, MultipartFile file) {
        try {
            // 유저 확인
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            // 1. 파일 업로드 수행
            // FileUploadService의 upload 메서드가 반환하는 타입을 확인하세요.
            var uploadedFile = fileUploadService.upload(file);

            // 2. 승인 요청 객체 생성
            UserUpdateApproval approval = UserUpdateApproval.builder()
                    .user(user)
                    .requestCompany(userUpdateRequest.getRequestCompany())
                    .requestBusinessNumber(userUpdateRequest.getRequestBusinessNumber())
                    .requestFileId(uploadedFile.getFileId()) // 💡 DB에 저장된 파일 PK 값
                    .status(RequestStatus.PENDING)
                    .build();

            userUpdateApprovalRepository.save(approval);

        } catch (IOException e) {
            // 파일 저장 실패 시 예외 처리
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }
}
