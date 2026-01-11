package com.teamgold.goldenharvest.domain.master.command.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_sku")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sku {

    @Id
    @Column(name = "sku_no", length = 20)
    private String skuNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "item_code", referencedColumnName = "item_code", nullable = false),
            @JoinColumn(name = "variety_code", referencedColumnName = "variety_code", nullable = false)
    })
    private Variety variety;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code", insertable = false, updatable = false)
    private ProduceMaster produceMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_code", nullable = false)
    private Grade grade;

    @Builder
    protected Sku(String skuNo,
                  Variety variety,
                  Grade grade) {
        this.skuNo = skuNo;
        this.variety = variety;
        this.produceMaster = variety.getProduceMaster();
        this.grade = grade;
    }
}