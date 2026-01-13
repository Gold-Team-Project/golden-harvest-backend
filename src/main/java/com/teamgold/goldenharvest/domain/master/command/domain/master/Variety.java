package com.teamgold.goldenharvest.domain.master.command.domain.master;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_variety")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(VarietyId.class)
public class Variety {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code", nullable = false)
    private ProduceMaster produceMaster;

    @Id
    @Column(name = "variety_code", length = 8)
    private String varietyCode;

    @Column(nullable = false, length = 20)
    private String varietyName;

    @Builder
    protected Variety(String varietyCode, ProduceMaster produceMaster, String varietyName) {
        this.varietyCode = varietyCode;
        this.produceMaster = produceMaster;
        this.varietyName = varietyName;
    }
}