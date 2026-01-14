package com.teamgold.goldenharvest.domain.sales.query.application.service;

import com.teamgold.goldenharvest.domain.sales.query.application.dto.OrderHistoryResponse;
import java.util.List;

public interface SalesOrderQueryService {
    // 주문 리스트 조회
    List<OrderHistoryResponse> getMyOrderHistory(String userEmail);
}