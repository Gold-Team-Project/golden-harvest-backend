package com.teamgold.goldenharvest.domain.master.command.application.dto.response.price;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Builder
public class PriceResponse {

    private String itemCode; //품목코드
    private String itemName; //품목명
    private String kindCode; //품종코드
    private String kindName; //품종명
    private String rank; // 등급코드
    private String unit; //다위

    private BigDecimal dpr1; //당일가격
    }
