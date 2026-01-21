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
@Table(name = "tb_sales_order_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SalesOrderStatus {
    @Id
    @Column(name = "sales_status_id", nullable = false)
    private Long salesStatusId; // 고유 pk

    @Column(name = "sales_status_name", length = 20)
    private String salesStatusName; // 배송 상태 한글 이름 표기

    @Column(name = "sales_status_type", length = 20)
    private String salesStatusType; // 배송 상태 영어 이름 표기
}