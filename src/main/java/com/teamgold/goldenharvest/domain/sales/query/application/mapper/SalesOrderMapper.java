package com.teamgold.goldenharvest.domain.sales.query.application.mapper;

import com.teamgold.goldenharvest.domain.sales.query.application.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SalesOrderMapper {
    List<OrderHistoryResponse> findOrderHistoryByUserEmail(String userEmail);
    OrderHistoryResponse findOrderDetailBySalesOrderId(String salesOrderId);

    List<AdminOrderHistoryResponse> findAllOrderHistory(AdminOrderSearchCondition searchCondition);

    AdminOrderDetailResponse findAdminOrderDetailBySalesOrderId(String salesOrderId);
}
