package com.teamgold.goldenharvest.domain.master.query.dto.response.master;

import lombok.Getter;
import java.time.LocalDate;

@Getter
public class MasterDataListResponse {
    private String skuNo; //sku 코드
    private String itemCode; //품목 코드
    private String itemName; //품목 정보
    private String varietyName; //품종 이름
    private String gradeName; //등급
    private String status; //상태
    private LocalDate createdAt; //등록일
    private LocalDate updatedAt; //수정일


}
