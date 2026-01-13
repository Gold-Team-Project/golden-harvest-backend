package com.teamgold.goldenharvest.domain.sales.query.application.service;

import com.teamgold.goldenharvest.domain.sales.query.application.dto.response.MyOrderResponse;
import java.util.List;

public interface SalesOrderQueryService {
    List<MyOrderResponse> getMyOrders(String userEmail);
}
