package com.teamgold.goldenharvest.domain.master.command.application.dto.request.master;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MasterDataRequest {
    private String pStartday;            // 조회 시작기간
    private String pEndday;              // 조회  종료기간
    private String pCountrycode;         // 지역코드
    private String pItemcategorycode;    // 부류코드
    private String pItemcode;            // 품목코드
    private String pKindcode;             // 품종코드
    private String pProductrankcode;     // 등급코드
}