package com.teamgold.goldenharvest.domain.sales.command.domain.sales_order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_sales_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SalesOrderStatus {
    @Id
    @Column(name = "sales_status_id", length = 20, nullable = false)
    private String salesStatusId;

    @Column(name = "sales_status_name", length = 20)
    private String salesStatusName;

    @Column(name = "sales_status_type", length = 20)
    private String salesStatusType;
}