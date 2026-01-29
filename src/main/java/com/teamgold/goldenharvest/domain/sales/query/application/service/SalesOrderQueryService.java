package com.teamgold.goldenharvest.domain.sales.query.application.service;

import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderDetailResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderHistoryResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderSearchCondition;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.MyOrderSearchCondition;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.OrderHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SalesOrderQueryService {

    Page<OrderHistoryResponse> getMyOrderHistory(String userEmail, MyOrderSearchCondition searchCondition, Pageable pageable);

    OrderHistoryResponse getOrderDetail(String salesOrderId);

    Page<AdminOrderHistoryResponse> getAllOrderHistory(AdminOrderSearchCondition searchCondition, Pageable pageable);

    AdminOrderDetailResponse getAdminOrderDetail(String salesOrderId);
}
