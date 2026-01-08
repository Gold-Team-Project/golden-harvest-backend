package com.teamgold.goldenharvest.domain.inventory.command.domain.discard;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_discard_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DiscardStatus {

    @Id
    @Column(name = "discard_status", length = 8, nullable = false)
    private String discardStatus;

    @Column(name = "status_name", length = 20)
    private String statusName;

    @Column(name = "description", length = 255)
    private String description;

    @Builder
    public DiscardStatus(String discardStatus,
                         String statusName,
                         String description) {
        this.discardStatus = discardStatus;
        this.statusName = statusName;
        this.description = description;
    }
}
