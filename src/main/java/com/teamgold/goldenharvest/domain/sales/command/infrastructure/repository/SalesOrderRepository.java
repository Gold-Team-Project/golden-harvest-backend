package com.teamgold.goldenharvest.domain.sales.command.infrastructure.repository;

import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, String> {
    List<SalesOrder> findByUserEmailOrderByCreatedAtDesc(String userEmail);
}
