package com.teamgold.goldenharvest.domain.master.command.application.dto.request.price;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PriceRequest {
    private String product_cls_code; // 구분 (소매, 도매)
    private String item_category_code; //부류코드
    private String p_country_code; //지역코드
    private String p_regday; //날짜


}
