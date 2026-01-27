package com.teamgold.goldenharvest.domain.sales.command.domain.sales_order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_sales_order_item")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderItem {
    @Id
    @Column(name = "sales_order_item_id", length = 36, nullable = false)
    private String salesOrderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id")
    private SalesOrder salesOrder;

    @Column(name = "sku_no", length = 20, nullable = false)
    private String skuNo;

    @Column(name = "quantity")
    private Integer quantity; // 수량

    @Column(name = "price")
    private BigDecimal price; // 가격
}
