package com.teamgold.goldenharvest.domain.master.command.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_variety")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Variety {

    @Id
    @Column(length = 8)
    private String varietyCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ProduceMaster produceMaster;

    @Column(nullable = false, length = 20)
    private String varietyName;

    @Builder
    protected Variety(String varietyCode, ProduceMaster produceMaster, String varietyName) {
        this.varietyCode = varietyCode;
        this.produceMaster = produceMaster;
        this.varietyName = varietyName;
    }
}
