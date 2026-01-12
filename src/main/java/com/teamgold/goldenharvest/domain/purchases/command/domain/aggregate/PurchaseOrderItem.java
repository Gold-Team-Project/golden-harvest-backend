package com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_purchase_order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseOrderItem {

    @Id
    @Column(name = "purchase_order_item_id", length = 20, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "purchase_order_id",
            nullable = false
    )
    private PurchaseOrder purchaseOrder;

    @Column(name = "sku_no", length = 20, nullable = false)
    private String skuNo;

    @Column(name = "quantity")
    private Integer quantity;
}

