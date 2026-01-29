package com.teamgold.goldenharvest.domain.sales.query.application.mapper;

import com.teamgold.goldenharvest.domain.sales.query.application.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable; // Pageable import

import java.util.List;

@Mapper
public interface SalesOrderMapper {
    List<OrderHistoryResponse> findOrderHistoryByUserEmail(@Param("userEmail") String userEmail, @Param("condition") MyOrderSearchCondition condition, @Param("pageable") Pageable pageable);
    long countOrderHistoryByUserEmail(@Param("userEmail") String userEmail, @Param("condition") MyOrderSearchCondition condition);

    OrderHistoryResponse findOrderDetailBySalesOrderId(String salesOrderId);

    List<AdminOrderHistoryResponse> findAllOrderHistory(@Param("searchCondition") AdminOrderSearchCondition searchCondition, @Param("pageable") Pageable pageable);
    long countAllOrderHistory(@Param("searchCondition") AdminOrderSearchCondition searchCondition);

    AdminOrderDetailResponse findAdminOrderDetailBySalesOrderId(String salesOrderId);
}
