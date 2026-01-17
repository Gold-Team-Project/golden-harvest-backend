package com.teamgold.goldenharvest.domain.sales.query.application.mapper;

import com.teamgold.goldenharvest.domain.sales.query.application.dto.OrderHistoryItem;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.OrderHistoryResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SalesOrderMapper {
    List<OrderHistoryResponse> findOrderHistoryByUserEmail(String userEmail);
    List<OrderHistoryItem> findOrderItemsBySalesOrderId(String salesOrderId);

    OrderHistoryResponse findOrderDetailBySalesOrderId(String salesOrderId);
}
