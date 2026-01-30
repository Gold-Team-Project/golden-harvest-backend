package com.teamgold.goldenharvest.domain.auth.command.application.service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.common.infra.file.domain.File;
import com.teamgold.goldenharvest.common.infra.file.service.FileUploadService;
import com.teamgold.goldenharvest.common.security.jwt.JwtProperties;
import com.teamgold.goldenharvest.common.security.jwt.JwtTokenProvider;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.LoginRequest;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.PasswordResetRequest;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.request.SignUpRequest;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.response.UserResponse;
import com.teamgold.goldenharvest.domain.user.command.application.event.dto.UserUpdatedEvent;
import com.teamgold.goldenharvest.domain.auth.command.application.dto.response.TokenResponse;
import com.teamgold.goldenharvest.domain.user.command.domain.Role;
import com.teamgold.goldenharvest.domain.user.command.domain.User;
import com.teamgold.goldenharvest.domain.user.command.domain.UserStatus;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.RoleRepository;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtProperties jwtProperties;
    private final FileUploadService fileUploadService;

    // User 정보 이벤트 리스너 (정동욱)
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void signup(SignUpRequest signUpRequest, MultipartFile file) {
        // 이메일 중복 여부 확인
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        //  이메일 인증 여부 확인
        String isVerified = (String) redisTemplate.opsForValue().get("EMAIL_VERIFIED:" +  signUpRequest.getEmail());

        //  인증되지 않은 이메일인 경우 예외 발생
        if (isVerified == null || !isVerified.equals("true")) {
            throw new BusinessException(ErrorCode.EMAIL_VERIFICATION_REQUIRED);
        }

        //  파일 업로드 tb_inquiry_file 테이블에 데이터가 쌓이고 File 객체가 반환
        File uploadedFile;
        try {
            uploadedFile = fileUploadService.upload(file);
        } catch (IOException e) {
            log.error("회원가입 중 파일 업로드 실패: {}", e.getMessage());
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
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
                .status(UserStatus.PENDING)
                .role(role)
                .fileUrl(uploadedFile.getFileUrl())
                .build();

        userRepository.save(user);

        // 가입 완료 후 redis에 남아있는 인증 성공 기록 삭제
        redisTemplate.delete("EMAIL_VERIFIED:" +  signUpRequest.getEmail());

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

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        //  이메일로 사용자 조회
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        //  비밀번호 검증
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        if (user.getStatus().equals(UserStatus.PENDING)) {
            // 관리자 승인이 필요한 상태
            throw new BusinessException(ErrorCode.USER_NOT_APPROVED);
        }
        //  계정 상태 확인
        if (!user.getStatus().equals(UserStatus.ACTIVE)) {
            throw new BusinessException(ErrorCode.USER_INACTIVE);
        }
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        redisTemplate.opsForValue().set(
                "RT:" + user.getEmail(),
                refreshToken,
                Duration.ofMillis(jwtProperties.getRefreshTokenExpiration())
        );

        return new TokenResponse(accessToken, refreshToken);

    }

    @Override
    public TokenResponse reissue(String refreshToken) {
        // 1. Refresh Token 자체의 유효성 및 서명 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        // 2. 토큰에서 유저 이메일 추출
        String email = jwtTokenProvider.getEmailFromToken(refreshToken);

        // 3. Redis에 저장된 원본 토큰 가져오기
        String savedRefreshToken = (String) redisTemplate.opsForValue().get("RT:" + email);

        // 4. Redis에 토큰이 없거나, 클라이언트가 보낸 토큰과 일치하지 않으면 예외 발생
        if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        // 5. 새로운 토큰 세트 생성 (Access + Refresh 모두 갱신)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.createAccessToken(user);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user);

        // 6. Redis의 기존 리프레시 토큰 업데이트 (Rotation)
        redisTemplate.opsForValue().set(
                "RT:" + email,
                newRefreshToken,
                Duration.ofMillis(jwtProperties.getRefreshTokenExpiration())
        );

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(String accessToken, String email) {
        // Redis에 저장된 Refresh Token이 있는지 확인
        String redisKey = "RT:" + email;

        log.info("[Logout Debug] 삭제 시도할 키: [{}]", redisKey);
        log.info("[Logout Debug] 해당 키가 Redis에 존재하는가? : {}", redisTemplate.hasKey(redisKey));

        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            // 존재한다면 삭제
            redisTemplate.delete(redisKey);
            log.info("유저 {} 로그아웃: Redis에서 리프레시 토큰 삭제 완료", email);
        }
        Long expiration = jwtTokenProvider.getExpiration(accessToken);

        if (expiration > 0) {
            // Redis에 "BL:토큰값" 형태로 남은 시간만큼 저장
            redisTemplate.opsForValue().set(
                    "BL:" + accessToken,
                    "logout",
                    Duration.ofMillis(expiration)
            );
            log.info("액세스 토큰 블랙리스트 등록 완료 (남은 시간: {}ms)", expiration);
        }
    }

    @Override//  비밀번호 재설정(비밀번호 찾기)
    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        //  redis에서 인증 여부 확인
        String isVerified = (String) redisTemplate.opsForValue().get("EMAIL_VERIFIED:" + passwordResetRequest.getEmail());

        if (isVerified == null || !isVerified.equals("true")) {
            throw new BusinessException(ErrorCode.EMAIL_VERIFICATION_REQUIRED);
        }
        //  사용자 조회
        User user = userRepository.findByEmail(passwordResetRequest.getEmail())
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        //  새 비밀번호 암호화 및 업데이트
        user.updatePassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));

        //  인증 기록 및 기존 리프레시 토큰 삭제(모든 기기 로그아웃)
        redisTemplate.delete("EMAIL_VERIFIED:" + passwordResetRequest.getEmail());
        redisTemplate.delete("RT:" + passwordResetRequest.getEmail());

        log.info("[Golden Harvest] 비밀번호 재설정 완료: {}", passwordResetRequest.getEmail());
    }
    public UserResponse getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return UserResponse.from(user);
    }


}
