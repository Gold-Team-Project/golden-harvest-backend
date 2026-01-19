package com.teamgold.goldenharvest.domain.master.command.domain.master;

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

    @Column(length = 20)
    private String packUnitName;

    private String packToKg;
    private int shelfLifeDays;

    private Double storageTempMin;

    private Double storageTempMax;

    @Column(nullable = false)
    private Boolean isActive = false;

    @Column
    private String country;
    private Long fileId;
    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    private String description;

    @Builder
    protected ProduceMaster(String itemCode,
                            String itemName,
                            String baseUnit,
                            String packUnitName,
                            String packToKg,
                            int shelfLifeDays,
                            Double storageTempMin,
                            Double storageTempMax,
                            Boolean isActive,
                            String country,
                            String description,
                            Long fileId) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.baseUnit = baseUnit;
        this.packUnitName = packUnitName;
        this.packToKg = packToKg;
        this.shelfLifeDays = shelfLifeDays;
        this.storageTempMin = storageTempMin;
        this.storageTempMax = storageTempMax;
        this.isActive = isActive;
        this.country = country;
        this.description = description;
        this.fileId = fileId;
    }

    public void appendedMasterData(
            int shelfLifeDays,
            Double storageTempMin,
            Double storageTempMax,
            String description,
            Long fileId) {
        this.shelfLifeDays = shelfLifeDays;
        this.storageTempMin = storageTempMin;
        this.storageTempMax = storageTempMax;
        this.description = description;
        this.fileId = fileId;
    }

    public void updatedMasterData(
            int shelfLifeDays,
            Double storageTempMin,
            Double storageTempMax,
            String description,
            Long fileId) {
        if (shelfLifeDays != 0) this.shelfLifeDays = shelfLifeDays;
        if (storageTempMin != null) this.storageTempMin = storageTempMin;
        if (storageTempMax != null) this.storageTempMax = storageTempMax;
        if (description != null) this.description = description;
        this.fileId = fileId;
    }

    public void updateMasterDataStatus() {
        this.isActive = !this.isActive;
    }

}
