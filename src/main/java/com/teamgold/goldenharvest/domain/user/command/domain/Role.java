package com.teamgold.goldenharvest.domain.user.command.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_role")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    @Id
    @Column(length = 20)
    private String roleStatusId; //권한 상태 ID

    @Column(length = 20)
    private String roleStatusName; //권한 상태 이름

    @Column(length = 20)
    private String roleStatusType; //권한 상태 타입

    @Builder
    public Role(String roleStatusId, String roleStatusName, String roleStatusType) {
        this.roleStatusId = roleStatusId;
        this.roleStatusName = roleStatusName;
        this.roleStatusType = roleStatusType;
    }
}
