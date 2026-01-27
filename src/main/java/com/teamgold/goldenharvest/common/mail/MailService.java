package com.teamgold.goldenharvest.common.mail;

public interface MailService {
    void sendVerificationEmail(String toEmail, String type);

    void verifyCode(String email, String code);
}
