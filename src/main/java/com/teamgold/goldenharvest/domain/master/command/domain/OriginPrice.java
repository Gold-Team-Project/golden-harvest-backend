package com.teamgold.goldenharvest.domain.master.command.domain;

import com.teamgold.goldenharvest.domain.master.command.domain.Sku;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_origin_price")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OriginPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long priceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_no", nullable = false)
    private Sku sku;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal originPrice;

    @Column(nullable = false)
    private LocalDate createdAt;
}
