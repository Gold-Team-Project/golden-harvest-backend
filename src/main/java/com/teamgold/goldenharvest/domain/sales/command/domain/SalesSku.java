package com.teamgold.goldenharvest.domain.sales.command.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_sales_sku")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesSku {
    @Id
    @Column(name = "sku_no", length = 20)
    private String skuNo;

    @Column(name = "item_name", length = 20)
    private String itemName;

    @Column(name = "grade_name", length = 20)
    private String gradeName;

    @Column(name = "variety_name", length = 20)
    private String varietyName;
}
