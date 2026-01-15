package com.teamgold.goldenharvest.domain.master.query.dto.response.master;

import com.teamgold.goldenharvest.domain.master.query.dto.response.price.OriginPriceResponse;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class MasterDataDetailResponse {
    private String skuNo; //sku 코드
    private String itemCode; //품목 코드
    private String itemName; //품목 정보
    private String varietyName; //품종명
    private String baseUnit; //단위
    private String packToKg; //무게
    private String grade; //등급
    private LocalDate createdAt; //등록일
    private LocalDate updatedAt; //수정일
    private String country; //원산지
    private String description; //설명
    private int shelfLifeDays; //유통기한
    private Double storageTempMin; // 저장온도(최소)
    private Double storageTempMax; // 저장온도(최대)

    private List<OriginPriceResponse> originPrices; // 원가 저장(최대 7일)

    public void setOriginPrices(List<OriginPriceResponse> originPrices) {
        this.originPrices = originPrices;
    }

}
