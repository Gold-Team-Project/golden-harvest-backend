package com.teamgold.goldenharvest.domain.sales.command.infrastructure.sales_order;

import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalesOrderStatusRepository extends JpaRepository<SalesOrderStatus, Long> {
    Optional<SalesOrderStatus> findBySalesStatusType(String salesStatusType);
}
