package com.teamgold.goldenharvest.domain.sales.command.application.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkuInfoChangedEvent {
    private String skuNo; // SKU PK
    private String itemName; // 품목명
    private String gradeName; // 등급명
    private String varietyName; // 품종명
}
