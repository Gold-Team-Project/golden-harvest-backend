package com.teamgold.goldenharvest.domain.sales.command.application.dto;

import com.teamgold.goldenharvest.domain.inventory.query.dto.AvailableItemResponse;
import com.teamgold.goldenharvest.domain.sales.command.domain.SalesSku;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class RedisCartItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String skuNo;
    private String itemName;
    private String gradeName;
    private String varietyName;
    private int quantity;
    private BigDecimal unitPrice;

    public static RedisCartItem from(SalesSku salesSku, int quantity, BigDecimal unitPrice) {
        return RedisCartItem.builder()
                .skuNo(salesSku.getSkuNo())
                .itemName(salesSku.getItemName())
                .gradeName(salesSku.getGradeName())
                .varietyName(salesSku.getVarietyName())
                .quantity(quantity)
                .unitPrice(unitPrice)
                .build();
    }

    public static RedisCartItem from(AvailableItemResponse item, int quantity) {
        return RedisCartItem.builder()
                .skuNo(item.getSkuNo())
                .itemName(item.getItemName())
                .gradeName(item.getGradeName())
                .varietyName(item.getVarietyName())
                .quantity(quantity)
                .unitPrice(BigDecimal.valueOf(item.getCustomerPrice()))
                .build();
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }
}
