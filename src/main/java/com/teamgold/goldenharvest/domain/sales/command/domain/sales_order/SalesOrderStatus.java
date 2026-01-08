package com.teamgold.goldenharvest.domain.sales.command.domain.sales_order;


import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class SalesOrderStatus {
    @Id
    @Column(name = "sales_status_id", length = 20, nullable = false)
    private String salesStatusId;

    @Column(name = "sales_status_name", length = 20)
    private String salesStatusName;

    @Column(name = "sales_status_type", length = 20)
    private String salesStatusType;
}
