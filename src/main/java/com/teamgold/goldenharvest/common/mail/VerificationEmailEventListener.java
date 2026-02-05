package com.teamgold.goldenharvest.common.mail;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerificationEmailEventListener {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redis;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(VerificationEmailRequestedEvent e) {
        String toEmail = e.toEmail();
        String type = e.type();
        String code = e.code();

        String key = emailCodeKey(toEmail);

        // 1) Redis 저장(먼저)
        long t1 = System.nanoTime();
        try {
            log.info("[Step] redisSet=START key={}", key);
            redis.opsForValue().set(key, code, Duration.ofMinutes(5));
            log.info("[Step] redisSet=OK {}ms", (System.nanoTime() - t1) / 1_000_000);
        } catch (Exception ex) {
            log.error("[Fail] redisSet failed", ex);
            // 여기서 예외를 던져도 이미 커밋 이후라 DB 롤백은 없음.
            throw new BusinessException(ErrorCode.EMAIL_SEND_FAILED);
        }

        // 2) SMTP 전송
        long t2 = System.nanoTime();
        try {
            MimeMessage message = buildMessage(toEmail, type, code);

            log.info("[Step] smtpSend=START to={}", toEmail);
            mailSender.send(message);
            log.info("[Step] smtpSend=OK {}ms", (System.nanoTime() - t2) / 1_000_000);

            log.info("[Golden Harvest] 인증 메일 전송 성공: {}", toEmail);
        } catch (Exception ex) {
            // 메일 실패 시: Redis에 저장한 코드 제거(정합성 유지)
            redis.delete(key);
            log.error("[Fail] smtpSend failed (redis rollback)", ex);
            throw new BusinessException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    private String emailCodeKey(String email) {
        return "EMAIL_CODE:" + email;
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
