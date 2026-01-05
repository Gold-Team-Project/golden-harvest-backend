package com.teamgold.goldenharvest.domain.inventory.command.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_lot_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LotStatus {

    @Id
    @Column(name = "lot_status", length = 8, nullable = false, updatable = false)
    private String lotStatus;

    @Column(name = "status_name", length = 20)
    private String statusName;

    public LotStatus(String lotStatus, String statusName) {
        this.lotStatus = lotStatus;
        this.statusName = statusName;
    }
}