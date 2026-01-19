package com.teamgold.goldenharvest.domain.inventory.command.domain.lot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_lot_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LotStatus {

    @Id
	@Enumerated(EnumType.STRING)
    @Column(name = "lot_status", length = 8, nullable = false, updatable = false)
    private LotStatusType lotStatus;

    @Column(name = "status_name", length = 20)
    private String statusName;

	@Builder
    public LotStatus(LotStatusType lotStatus, String statusName) {
        this.lotStatus = lotStatus;
        this.statusName = statusName;
    }

	public enum LotStatusType {
		AVAILABLE,
		ALLOCATED,
		DEPLETED
	}
}