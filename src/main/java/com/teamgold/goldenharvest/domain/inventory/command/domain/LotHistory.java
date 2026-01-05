package com.teamgold.goldenharvest.domain.inventory.command.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_lot_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LotHistory {

    @Id
    @Column(name = "lot_history_id", length = 20, nullable = false, updatable = false)
    private String lotHistoryId;

    @Column(name = "lot_no", length = 20, nullable = false, updatable = false)
    private String lotNo;


    @Column(name = "from_status", length = 8)
    private String fromStatus;

    @Column(name = "to_status", length = 8)
    private String toStatus;

    @Column(name = "changed_at", nullable = false, updatable = false)
    private LocalDateTime changedAt;

    @Column(name = "changed_by", length = 255)
    private String changedBy;

    @Builder
    public LotHistory(
            String lotHistoryId,
            String lotNo,
            LocalDateTime changedAt,
            String changedBy
    ) {
        this.lotHistoryId = lotHistoryId;
        this.lotNo = lotNo;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
    }
}
