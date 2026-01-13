package com.teamgold.goldenharvest.domain.master.command.application.dto.response.master;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MasterResponse {
    private String itemName; //품목명
    private String kindName; //품종명
    private String kindCode; //품종코드
    private String baseUnit; // 기준단위
    private String unitSize; // 단위 크기
    private String countryName; // 원산지
    private String productRank; //등급
    private String itemCode; // 품목코드
}
