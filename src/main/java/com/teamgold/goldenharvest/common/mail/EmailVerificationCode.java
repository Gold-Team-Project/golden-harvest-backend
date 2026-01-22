package com.teamgold.goldenharvest.common.mail;

import java.security.SecureRandom;

public class EmailVerificationCode {

    private static final int CODE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    public static String getCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
