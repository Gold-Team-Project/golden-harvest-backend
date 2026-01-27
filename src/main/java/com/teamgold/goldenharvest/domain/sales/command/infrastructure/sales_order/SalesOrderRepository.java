package com.teamgold.goldenharvest.domain.sales.command.infrastructure.sales_order;

import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, String> {
}
