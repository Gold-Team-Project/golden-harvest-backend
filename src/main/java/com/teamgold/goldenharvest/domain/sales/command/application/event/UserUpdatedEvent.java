package com.teamgold.goldenharvest.domain.sales.command.application.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatedEvent {
    private String userEmail;
    private String newUserEmail; // 이메일의 상태 변경 시 를 대비하여 작성
}