package com.teamgold.goldenharvest.domain.sales.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    private String skuNo;
    private int quantity;
}
