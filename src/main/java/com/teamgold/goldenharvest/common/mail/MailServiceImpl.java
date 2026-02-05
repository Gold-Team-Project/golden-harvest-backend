package com.teamgold.goldenharvest.common.mail;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final UserRepository userRepository;
    private final Environment env;
    private final ApplicationEventPublisher publisher;
    private final StringRedisTemplate redis;

    @Override
    @Transactional(readOnly = true)
    public void sendVerificationEmail(String toEmail, String type) {

        log.info("[RedisDebug] host={}, port={}, sslEnabled={}",
                env.getProperty("spring.data.redis.host"),
                env.getProperty("spring.data.redis.port"),
                env.getProperty("spring.data.redis.ssl.enabled")
        );
        log.info("[MailDebug] toEmail={}, type={}", toEmail, type);

        // 1) DB 조회/검증 (트랜잭션 안에서 짧게 끝)
        boolean isExist = checkUserExistFast(toEmail);

        // 2) 타입 검증
        validateType(type, isExist);

        // 3) 코드 생성
        String code = EmailVerificationCode.getCode();

        // 4) 커밋 이후에 처리될 이벤트 발행
        publisher.publishEvent(new VerificationEmailRequestedEvent(toEmail, type, code));

        log.info("[Golden Harvest] 인증메일 요청 이벤트 발행 완료: {}", toEmail);
    }

    @Override
    public void verifyCode(String email, String code) {
        String saved = redis.opsForValue().get(emailCodeKey(email));
        if (saved == null || !saved.equals(code)) {
            throw new BusinessException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        redis.opsForValue().set(emailVerifiedKey(email), "true", Duration.ofMinutes(10));
        redis.delete(emailCodeKey(email));
        log.info("[Golden Harvest] 이메일 인증 성공: {}", email);
    }

    private boolean checkUserExistFast(String email) {
        long t0 = System.nanoTime();
        try {
            boolean exist = userRepository.existsByEmail(email);
            log.info("[Timing] existsByEmail={}ms", (System.nanoTime() - t0) / 1_000_000);
            return exist;
        } catch (Exception e) {
            log.error("[Fail] existsByEmail query failed", e);
            throw new BusinessException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    private String emailCodeKey(String email) {
        return "EMAIL_CODE:" + email;
    }

    private String emailVerifiedKey(String email) {
        return "EMAIL_VERIFIED:" + email;
    }

    private void validateType(String type, boolean isExist) {
        if ("signup".equals(type) && isExist) throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        if ("password".equals(type) && !isExist) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        if (!"signup".equals(type) && !"password".equals(type)) throw new BusinessException(ErrorCode.EMAIL_SEND_FAILED);
    }
}
