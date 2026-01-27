package com.teamgold.goldenharvest.common.mail;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final RedisTemplate<String,Object> redisTemplate;
    private final UserRepository userRepository;


    // 메일 발송 및 redis 저장
    @Override
    public void sendVerificationEmail(String toEmail, String type) {

        log.info("[MailDebug] 입력받은 이메일: {}, 입력받은 타입: [{}]", toEmail, type);

        //  가입 여부 확인
        boolean isExist = userRepository.existsByEmail(toEmail);
        if (type.equals("signup") && isExist) {
            //  회원가입 시 이미 계정이 있는 경우
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        } else if(type.equals("password") && !isExist) {
            //  계정이 없는경우
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        String code = EmailVerificationCode.getCode();

        try{
            String subject = type.equals("signup")
                    ? "[골든하베스트] 회원가입 인증번호 안내"
                    : "[골든하베스트] 비밀번호 재설정 인증번호 안내";

            String title = type.equals("signup") ? "회원가입" : "비밀번호 재설정";
            String description = type.equals("signup")
                    ? "안전한 회원가입을 위해"
                    : "비밀번호 재설정을 위해";

            MimeMessage message = createEmailForm(toEmail, code, subject, title, description);
            mailSender.send(message);

            //  redis에 5분간 저장
            redisTemplate.opsForValue().set(
                    "EMAIL_CODE:" + toEmail,
                    code,
                    Duration.ofMinutes(5)
            );
            log.info("[Golden Harvest] 인증 메일 전송 성공: {}", toEmail);
        } catch (Exception e){
            log.error("[Golden Harvest] 메일 발송 실패: {}", e.getMessage());
            throw new BusinessException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    @Override
    public void verifyCode(String email, String code) {
        String saveCode = (String) redisTemplate.opsForValue().get("EMAIL_CODE:" + email);

        //  만료되었거나 코드가 일치하지 않는 경우
        if(saveCode == null || !saveCode.equals(code)){
            throw new BusinessException(ErrorCode.INVALID_VERIFICATION_CODE);
        }
        redisTemplate.opsForValue().set(
                "EMAIL_VERIFIED:" + email,
                "true",
                Duration.ofMinutes(10)
        );

        //  인증 성공 시 redis에서 삭제
        redisTemplate.delete("EMAIL_CODE:" + email);
        log.info("[Golden Harvest] 이메일 인증 성공: {}", email);
    }

    //  메일 폼 생성
    private MimeMessage createEmailForm(String toEmail, String code, String subject, String title, String description) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject(subject);

        // HTML 템플릿
        StringBuilder msg = new StringBuilder();
        msg.append("<div style='margin:20px; border:1px solid #eee; padding:20px; border-radius:10px;'>")
                .append("<h1 style='color: #f39c12;'>Golden Harvest</h1>")
                .append("<p>안녕하세요, <strong>Golden Harvest</strong>입니다.</p>")
                .append("<p>요청하신 <strong>").append(title).append("</strong>을(를) 위한 인증번호입니다.</p>")
                .append("<p>아래의 인증번호를 입력해 주세요.</p>")
                .append("<div style='background:#f9f9f9; border:1px dashed #f39c12; padding:20px; font-size:30px; letter-spacing:8px; font-weight:bold; text-align:center; color:#333;'>")
                .append(code)
                .append("</div>")
                .append("<p style='margin-top:20px;'>인증번호 유효 시간은 <strong>5분</strong>입니다.</p>")
                .append("<hr style='border:0;border-top:1px solid #eee;'>")
                .append("<p style='font-size:12px; color:gray;'>본 메일은 발신 전용입니다.<br>문의: support@goldenharvest.com</p>")
                .append("</div>");

        helper.setText(msg.toString(), true);
        helper.setFrom(new InternetAddress("noreply@goldenharvest.com", "Golden Harvest 관리자"));

        return message;
    }
}

