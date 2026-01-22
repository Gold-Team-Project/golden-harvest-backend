package com.teamgold.goldenharvest.domain.sales.command.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCartItemRequest {
    private String skuNo;
    private int quantity;
}
