package com.teamgold.goldenharvest.domain.inventory.command.domain.discard;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_discard")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Discard {

    @Id
    @Column(name = "discard_id", length = 20, nullable = false)
    private String discardId;

    @Column(name = "lot_no", length = 20, nullable = false)
    private String lotNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discard_status", nullable = false)
    private DiscardStatus discardStatus;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "discarded_at")
    private LocalDateTime discardedAt;

    @Column(name = "approved_by", length = 255)
    private String approvedBy;

    @Column(name = "discard_rate", precision = 10, scale = 2)
    private BigDecimal discardRate;

    @Builder
    public Discard(
            String discardId,
            String lotNo,
            DiscardStatus discardStatus,
            Integer quantity,
            LocalDateTime discardedAt,
            String approvedBy,
            BigDecimal discardRate
    ) {
        this.discardId = discardId;
        this.lotNo = lotNo;
        this.discardStatus = discardStatus;
        this.quantity = quantity;
        this.discardedAt = discardedAt;
        this.approvedBy = approvedBy;
        this.discardRate = discardRate;
    }
}
