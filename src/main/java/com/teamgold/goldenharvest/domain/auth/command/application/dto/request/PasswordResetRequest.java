package com.teamgold.goldenharvest.domain.auth.command.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {
    @Email(message = "이메일 형식이 올바르지 않습니다..")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotBlank(message = "새로운 비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이여야 합니다.")
    private String newPassword;
}
