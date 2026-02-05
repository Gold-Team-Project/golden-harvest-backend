package com.teamgold.goldenharvest.common.mail;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redis;     // ✅ StringRedisTemplate로 변경
    private final UserRepository userRepository;
    private final Environment env;

    @Override
    public void sendVerificationEmail(String toEmail, String type) {

        log.info("[RedisDebug] host={}, port={}, sslEnabled={}",
                env.getProperty("spring.data.redis.host"),
                env.getProperty("spring.data.redis.port"),
                env.getProperty("spring.data.redis.ssl.enabled")
        );
        log.info("[MailDebug] toEmail={}, type={}", toEmail, type);

        // 1) DB 조회는 최대한 빨리 끝내기 (메일/레디스 이전에 끝)
        boolean isExist = checkUserExistFast(toEmail);

        // 2) 타입 검증
        validateType(type, isExist);

        // 3) 코드 생성
        String code = EmailVerificationCode.getCode();

        // 4) 메일 작성
        MimeMessage message = buildMessage(toEmail, type, code);

        // 5) SMTP 전송 (느릴 수 있음)
        long t1 = System.nanoTime();
        try {
            log.info("[Step] smtpSend=START to={}", toEmail);
            mailSender.send(message);
            log.info("[Step] smtpSend=OK {}ms", (System.nanoTime() - t1) / 1_000_000);
        } catch (Exception e) {
            log.error("[Fail] smtpSend failed", e);
            throw new BusinessException(ErrorCode.EMAIL_SEND_FAILED);
        }

        // 6) Redis 저장 (문자열로 단순 저장)
        String key = emailCodeKey(toEmail);
        long t2 = System.nanoTime();
        try {
            log.info("[Step] redisSet=START key={}", key);
            redis.opsForValue().set(key, code, Duration.ofMinutes(5));
            log.info("[Step] redisSet=OK {}ms", (System.nanoTime() - t2) / 1_000_000);
        } catch (Exception e) {
            log.error("[Fail] redisSet failed", e);
            throw new BusinessException(ErrorCode.EMAIL_SEND_FAILED);
        }

        log.info("[Golden Harvest] 인증 메일 전송 성공: {}", toEmail);
    }

    /**
     * ✅ DB조회만 담당 (메일/레디스와 완전히 분리)
     */
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

    private void validateType(String type, boolean isExist) {
        if ("signup".equals(type) && isExist) throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        if ("password".equals(type) && !isExist) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        if (!"signup".equals(type) && !"password".equals(type)) throw new BusinessException(ErrorCode.EMAIL_SEND_FAILED);
    }

    private MimeMessage buildMessage(String toEmail, String type, String code) {
        try {
            String subject = "signup".equals(type)
                    ? "[골든하베스트] 회원가입 인증번호 안내"
                    : "[골든하베스트] 비밀번호 재설정 인증번호 안내";

            String title = "signup".equals(type) ? "회원가입" : "비밀번호 재설정";

            return createEmailForm(toEmail, code, subject, title);
        } catch (Exception e) {
            log.error("[Fail] createEmailForm failed", e);
            throw new BusinessException(ErrorCode.EMAIL_SEND_FAILED);
        }
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

    private String emailCodeKey(String email) {
        return "EMAIL_CODE:" + email;
    }

    private String emailVerifiedKey(String email) {
        return "EMAIL_VERIFIED:" + email;
    }

    private MimeMessage createEmailForm(String toEmail, String code, String subject, String title) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject(subject);

        String html = """
            <div style='margin:20px; border:1px solid #eee; padding:20px; border-radius:10px;'>
              <h1 style='color: #f39c12;'>Golden Harvest</h1>
              <p>안녕하세요, <strong>Golden Harvest</strong>입니다.</p>
              <p>요청하신 <strong>%s</strong>을(를) 위한 인증번호입니다.</p>
              <p>아래의 인증번호를 입력해 주세요.</p>
              <div style='background:#f9f9f9; border:1px dashed #f39c12; padding:20px; font-size:30px; letter-spacing:8px; font-weight:bold; text-align:center; color:#333;'>
                %s
              </div>
              <p style='margin-top:20px;'>인증번호 유효 시간은 <strong>5분</strong>입니다.</p>
              <hr style='border:0;border-top:1px solid #eee;'>
              <p style='font-size:12px; color:gray;'>본 메일은 발신 전용입니다.</p>
            </div>
            """.formatted(title, code);

        helper.setText(html, true);
        helper.setFrom(new InternetAddress("noreply@goldenharvest.com", "Golden Harvest 관리자"));
        return message;
    }
}
