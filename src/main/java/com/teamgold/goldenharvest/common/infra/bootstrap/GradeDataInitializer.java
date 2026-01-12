package com.teamgold.goldenharvest.common.infra.bootstrap;

import com.teamgold.goldenharvest.domain.master.command.domain.master.Grade;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.GradeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GradeDataInitializer implements CommandLineRunner {

    private final GradeRepository gradeRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (gradeRepository.count() > 0) {
            return;
        }

        List<Grade> grades = List.of(
                grade("01", "1등급", 2),
                grade("02", "2등급", 3),
                grade("03", "3등급", 4),
                grade("04", "상품", 1),
                grade("05", "중품", 2),
                grade("06", "하품", 3),
                grade("07", "유기농", 1),
                grade("08", "무농약", 2),
                grade("09", "저농약", 3),
                grade("10", "냉장", 1),
                grade("11", "냉동", 3),
                grade("12", "무항생제", 4),
                grade("13", "S과", 1),
                grade("14", "M과", 2),
                grade("15", "M과", 1),
                grade("16", "S과", 2),
                grade("17", "1+등급", 1),
                grade("18", "등급외저란", 5),
                grade("19", "특大", 1),
                grade("20", "大", 2),
                grade("21", "中", 3),
                grade("22", "小", 4),
                grade("23", "2L과", 4),
                grade("24", "L과", 1),
                grade("25", "M과", 2),
                grade("26", "S과", 3),
                grade("27", "대멸", 1),
                grade("28", "중멸", 2),
                grade("29", "세멸", 3)
        );

        gradeRepository.saveAll(grades);
    }

    private Grade grade(String code, String name, int rank) {
        return Grade.builder()
                .gradeCode(code)
                .gradeName(name)
                .gradeRank(rank)
                .build();
    }
}
