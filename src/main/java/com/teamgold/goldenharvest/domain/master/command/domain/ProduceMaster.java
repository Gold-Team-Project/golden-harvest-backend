package com.teamgold.goldenharvest.domain.master.command.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "tb_produce_master")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProduceMaster {
    @Id
    @Column(length = 8)
    private String itemCode;

    @Column(length = 20)
    private String itemName;

    @Column(length = 20)
    private String baseUnit;

    private int shelfLifeDays;

    private Double storageTempMin;

    private Double storageTempMax;

    @Column(nullable = false)
    private Boolean isActive;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    @Builder
    protected ProduceMaster(String itemCode,
                            String itemName,
                            String baseUnit,
                            int shelfLifeDays,
                            Double storageTempMin,
                            Double storageTempMax,
                            Boolean isActive,
                            LocalDate createdAt,
                            LocalDate updatedAt) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.baseUnit = baseUnit;
        this.shelfLifeDays = shelfLifeDays;
        this.storageTempMin = storageTempMin;
        this.storageTempMax = storageTempMax;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
