package com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_purchase_order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PurchaseOrder {

    @Id
    @Column(name = "purchase_order_id", length = 20, nullable = false)
    private String purchase_order_id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "order_status_id",
            nullable = false
    )
    private OrderStatus orderStatus;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "sku_no", length = 20, nullable = false)
    private String skuNo;

    @Column(name = "quantity")
    private Integer quantity;
}
