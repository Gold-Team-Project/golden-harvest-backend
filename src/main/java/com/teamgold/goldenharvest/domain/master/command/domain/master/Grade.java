package com.teamgold.goldenharvest.domain.master.command.domain.master;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_grade")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grade {

    @Id
    @Column(length = 8)
    private String gradeCode;

    @Column(nullable = false, length = 20)
    private String gradeName;

    @Column(nullable = false)
    private int gradeRank;

    @Builder
    protected Grade(String gradeCode, String gradeName, Integer gradeRank) {
        this.gradeCode = gradeCode;
        this.gradeName = gradeName;
        this.gradeRank = gradeRank;
    }
}
