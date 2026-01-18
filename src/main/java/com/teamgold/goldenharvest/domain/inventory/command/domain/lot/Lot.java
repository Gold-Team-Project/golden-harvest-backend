package com.teamgold.goldenharvest.domain.inventory.command.domain.lot;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tb_lot")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Lot {

    @Id
    @Column(name = "lot_no", length = 20, nullable = false, updatable = false)
    private String lotNo;

    @Column(name = "inbound_id", length = 20, nullable = false, updatable = false)
    private String inboundId;

    @Column(name = "sku_no", length = 20, nullable = false, updatable = false)
    private String skuNo;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "inbound_date")
    private LocalDate inboundDate;

    @Column(name = "lot_status", nullable = false)
    private LotStatus.LotStatusType lotStatus;

    @Builder
    public Lot (
            String lotNo,
            String inboundId,
            String skuNo,
            Integer quantity,
            LocalDate inboundDate,
            LotStatus.LotStatusType lotStatus
    ) {
        this.lotNo = lotNo;
        this.inboundId = inboundId;
        this.skuNo = skuNo;
        this.quantity = quantity;
        this.inboundDate = inboundDate;
        this.lotStatus = lotStatus;
    }

	public Integer consumeQuantity(Integer quantity) {
		int actualConsume = Math.min(this.quantity, quantity);
		this.quantity -= actualConsume;
		return actualConsume; // 실제로 이 Lot에서 차감된 수량 반환
	}
}
