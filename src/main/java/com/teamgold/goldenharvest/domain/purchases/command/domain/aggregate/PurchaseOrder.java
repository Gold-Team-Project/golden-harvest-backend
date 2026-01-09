package com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tb_purchase_order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseOrder {

    @Id
    @Column(name = "purchase_order_id", length = 20, nullable = false)
    private String id;

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

}
