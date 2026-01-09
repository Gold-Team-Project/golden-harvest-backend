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

    /**
     * 품목
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code", nullable = false)
    private ProduceMaster produceMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variety_code", nullable = false)
    private Variety variety;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_code", nullable = false)
    private Grade grade;

    @Builder
    protected Sku(String skuNo,
                  ProduceMaster produceMaster,
                  Variety variety,
                  Grade grade) {

        this.skuNo = skuNo;
        this.produceMaster = produceMaster;
        this.variety = variety;
        this.grade = grade;
    }
}
