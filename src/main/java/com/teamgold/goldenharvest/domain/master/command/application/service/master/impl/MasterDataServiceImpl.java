package com.teamgold.goldenharvest.domain.master.command.application.service.master.impl;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.common.infra.file.service.FileUploadService;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataAppendRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataUpdatedRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.response.master.MasterResponse;
import com.teamgold.goldenharvest.domain.master.command.application.event.MasterDataEventPublisher;
import com.teamgold.goldenharvest.domain.master.command.application.event.dto.ItemMasterUpdatedEvent;
import com.teamgold.goldenharvest.domain.master.command.application.service.master.MasterDataService;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Grade;
import com.teamgold.goldenharvest.domain.master.command.domain.master.ProduceMaster;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Sku;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Variety;
import com.teamgold.goldenharvest.domain.master.command.domain.price.OriginPrice;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.GradeRepository;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.MasterRepository;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.SkuRepository;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.VarietyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MasterDataServiceImpl implements MasterDataService {

    private final MasterRepository masterRepository;
    private final VarietyRepository varietyRepository;
    private final GradeRepository gradeRepository;
    private final SkuRepository skuRepository;
    private final FileUploadService fileUploadService;
    private final MasterDataEventPublisher eventPublisher;

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
                                .isActive(false)
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

    @Override
    @Transactional
    public void appendMasterData(
            String itemCode,
            MasterDataAppendRequest request,
            MultipartFile file) throws IOException {
        String fileUrl = null;

        if (file != null && !file.isEmpty()) {
            var savedFile = fileUploadService.upload(file);
            fileUrl = savedFile.getFileUrl();
        }
        ProduceMaster master = masterRepository.findById(itemCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.MASTER_DATA_NOT_FOUND));

        master.appendedMasterData(
                request.getShelfLifeDays(),
                request.getStorageTempMin(),
                request.getStorageTempMax(),
                request.getDescription(),
                fileUrl

        );
    }

    @Override
    @Transactional
    public void updateMasterDataStatus(String itemCode) {
        ProduceMaster master = masterRepository.findById(itemCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.MASTER_DATA_NOT_FOUND));

        master.updateMasterDataStatus();

    }

    @Override
    @Transactional
    public void updatedMasterData(
            String itemCode,
            MasterDataUpdatedRequest request,
            MultipartFile file
    ) throws IOException {
      String fileUrl = null;

        if (file != null && !file.isEmpty()) {
            var savedFile = fileUploadService.upload(file);
            fileUrl = savedFile.getFileUrl();
        }
        ProduceMaster master = masterRepository.findById(itemCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.MASTER_DATA_NOT_FOUND));

        master.updatedMasterData(
                request.getShelfLifeDays(),
                request.getStorageTempMin(),
                request.getStorageTempMax(),
                request.getDescription(),
                fileUrl
        );

    }

    @Override
    @Transactional
    public void publishAllMasterDataEvent() {
        List<Sku> skus = skuRepository.findAllWithDetails();

        for (Sku sku : skus) {
            eventPublisher.publishItemMasterUpdatedEvent(
                    ItemMasterUpdatedEvent.builder()
                            .skuNo(sku.getSkuNo())
                            .itemName(sku.getProduceMaster().getItemName())
                            .gradeName(sku.getGrade().getGradeName())
                            .varietyName(sku.getVariety().getVarietyName())
                            .fileUrl(sku.getProduceMaster().getFileUrl())
                            .baseUnit(sku.getProduceMaster().getBaseUnit())
                            .isActive(sku.getProduceMaster().getIsActive())
                            .build()
            );
        }
    }
}
