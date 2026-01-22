package com.teamgold.goldenharvest.domain.auth.command.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")
    private String password;

    @NotBlank(message = "상호명은 필수 입력 값입니다.")
    private String company;

    @NotBlank(message = "사업자번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^\\d{10}$", message = "사업자번호는 숫자 10자리여야 합니다.")
    private String businessNumber;

    @NotBlank(message = "담당자이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^01[0-9]-?\\d{3,4}-?\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phoneNumber;

    private Long fileId;

}
