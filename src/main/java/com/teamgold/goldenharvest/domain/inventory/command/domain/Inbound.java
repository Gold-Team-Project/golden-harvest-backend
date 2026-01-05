package com.teamgold.goldenharvest.domain.inventory.command.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tb_inbound")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Inbound {

    @Id
    @Column(name = "inbound_id", length = 20, nullable = false, updatable = false)
    private String inboundId;

    @Column(name = "purchase_order_item_id", length = 20, nullable = false, updatable = false)
    private String purchaseOrderItemId;

    @Column(name = "sku_no", length = 20, nullable = false, updatable = false)
    private String skuNo;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "inbound_date")
    private LocalDate inboundDate;

    @Builder
    public Inbound(
            String inboundId,
            String purchaseOrderItemId,
            String skuNo,
            Integer quantity,
            LocalDate inboundDate
    ) {
        this.inboundId = inboundId;
        this.purchaseOrderItemId = purchaseOrderItemId;
        this.skuNo = skuNo;
        this.quantity = quantity;
        this.inboundDate = inboundDate;
    }
}
