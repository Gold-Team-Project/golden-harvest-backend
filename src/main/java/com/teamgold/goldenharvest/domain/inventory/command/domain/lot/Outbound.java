package com.teamgold.goldenharvest.domain.inventory.command.domain.lot;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_outbound")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Outbound {

    @Id
    @Column(name = "outbound_id", length = 20, nullable = false, updatable = false)
    private String outboundId;

    @Column(name = "sales_order_item_id", length = 20, nullable = false, updatable = false)
    private String salesOrderItemId;

    @Column(name = "lot_no", length = 20, nullable = false, updatable = false)
    private String lotNo;

    @Column(name = "outbound_date")
    private LocalDate outboundDate;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "outbound_price", precision = 10, scale = 2)
    private BigDecimal outboundPrice;

    @Builder
    public Outbound(
            String outboundId,
            String salesOrderItemId,
            String lotNo,
            Integer quantity,
            LocalDate outboundDate,
            BigDecimal outboundPrice
    ) {
        this.outboundId = outboundId;
        this.salesOrderItemId = salesOrderItemId;
        this.lotNo = lotNo;
        this.quantity = quantity;
        this.outboundDate = outboundDate;
        this.outboundPrice = outboundPrice;
    }
}

