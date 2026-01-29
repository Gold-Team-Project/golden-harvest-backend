package com.teamgold.goldenharvest.domain.inventory.query.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PricePolicyRequest {
    String skuNo;
    BigDecimal marginRate;
}
