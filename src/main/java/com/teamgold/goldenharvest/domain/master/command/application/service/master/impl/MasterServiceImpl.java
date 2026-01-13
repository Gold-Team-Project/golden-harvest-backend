package com.teamgold.goldenharvest.domain.master.command.application.service.master.impl;

import com.teamgold.goldenharvest.domain.master.command.application.dto.response.master.MasterResponse;
import com.teamgold.goldenharvest.domain.master.command.application.service.master.MasterService;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Grade;
import com.teamgold.goldenharvest.domain.master.command.domain.master.ProduceMaster;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Sku;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Variety;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.GradeRepository;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.MasterRepository;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.SkuRepository;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.VarietyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MasterServiceImpl implements MasterService {

    private final MasterRepository masterRepository;
    private final VarietyRepository varietyRepository;
    private final GradeRepository gradeRepository;
    private final SkuRepository skuRepository;

    @Override
    @Transactional
    public void saveAll(List<MasterResponse> responses) {
        for (MasterResponse row : responses) {
            // 품목
            ProduceMaster master = masterRepository.findByItemName(row.getItemName())
                    .orElseGet(() -> {
                        ProduceMaster newMaster = ProduceMaster.builder()
                                .itemName(row.getItemName())
                                .itemCode(row.getItemCode())
                                .baseUnit(row.getBaseUnit())
                                .packToKg(row.getUnitSize())
                                .country(row.getCountryName())
                                .isActive(true)
                                .build();
                        return masterRepository.save(newMaster);
                    });

            // 품종
            Variety variety = Variety.builder()
                    .varietyCode(row.getKindCode())
                    .varietyName(row.getKindName())
                    .produceMaster(master)
                    .build();

            varietyRepository.save(variety);

            //등급
            String gradeCodes = row.getProductRank();
            if (gradeCodes == null || gradeCodes.isBlank()) {
                continue;
            }

            for (String gradeCode : gradeCodes.split(",")) {

                Grade grade = gradeRepository.findByGradeCode(gradeCode.trim())
                        .orElseThrow(() ->
                                new IllegalStateException("존재X")
                        );
                //SKU
                String skuNo = master.getItemCode()
                        + "-" + variety.getVarietyCode()
                        + "-" + grade.getGradeCode();

                if (skuRepository.existsBySkuNo(skuNo)) {
                    continue;
                }


                Sku sku = Sku.builder()
                        .skuNo(skuNo)
                        .variety(variety)
                        .grade(grade)
                        .build();
                skuRepository.save(sku);
            }


        }
    }
}
