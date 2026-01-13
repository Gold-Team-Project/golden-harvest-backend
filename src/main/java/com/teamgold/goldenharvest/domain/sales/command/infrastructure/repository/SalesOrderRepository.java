package com.teamgold.goldenharvest.domain.sales.command.infrastructure.repository;

import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, String> {
    // 주문 리스트 조회 - 15
    @EntityGraph(attributePaths = {"salesOrderItems"})
    List<SalesOrder> findByUserEmailOrderByCreatedAtDesc(String userEmail);
    List<SalesOrder> findByUserEmail(String userEmail);
}
