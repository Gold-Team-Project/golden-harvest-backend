package com.teamgold.goldenharvest.domain.master.command.application.dto.request.master;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterDataUpdatedRequest {
    private int shelfLifeDays;  //유통기한
    private Double storageTempMin; //저장온도(최소)
    private Double storageTempMax; //저장온도(최대)
    private String description; // 설명
}
