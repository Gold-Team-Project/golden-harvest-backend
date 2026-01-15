package com.teamgold.goldenharvest.domain.master.query.dto.response.price;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
public class OriginPriceResponse {
    private BigDecimal originPrice; //원가
    private String unit; //단위
    private LocalDate createdAt; //날짜


}
