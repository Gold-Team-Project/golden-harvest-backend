package com.teamgold.goldenharvest.domain.sales.query.application.service;

import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderDetailResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderHistoryResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderSearchCondition;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.OrderHistoryResponse;

import java.util.List;

public interface SalesOrderQueryService {

    List<OrderHistoryResponse> getMyOrderHistory(String userEmail);

    OrderHistoryResponse getOrderDetail(String salesOrderId);

    List<AdminOrderHistoryResponse> getAllOrderHistory(AdminOrderSearchCondition searchCondition);

    AdminOrderDetailResponse getAdminOrderDetail(String salesOrderId);
}
