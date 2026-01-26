package com.teamgold.goldenharvest.domain.sales.command.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddToCartRequest {
    private String skuNo;
    private int quantity;
}
