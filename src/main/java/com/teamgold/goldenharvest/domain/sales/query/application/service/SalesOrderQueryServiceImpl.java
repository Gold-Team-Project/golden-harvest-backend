package com.teamgold.goldenharvest.domain.sales.query.application.service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderDetailResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderHistoryResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderSearchCondition;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.OrderHistoryResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.MyOrderSearchCondition;
import com.teamgold.goldenharvest.domain.sales.query.application.mapper.SalesOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SalesOrderQueryServiceImpl implements SalesOrderQueryService {

    private final SalesOrderMapper salesOrderMapper;

    @Override
    public List<OrderHistoryResponse> getMyOrderHistory(String userEmail, MyOrderSearchCondition searchCondition) {
        return salesOrderMapper.findOrderHistoryByUserEmail(userEmail, searchCondition);
    }

    @Override
    public OrderHistoryResponse getOrderDetail(String salesOrderId) {
        OrderHistoryResponse orderDetail = salesOrderMapper.findOrderDetailBySalesOrderId(salesOrderId);
        if (orderDetail == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        return orderDetail;
    }

    @Override
    public List<AdminOrderHistoryResponse> getAllOrderHistory(AdminOrderSearchCondition searchCondition) {
        return salesOrderMapper.findAllOrderHistory(searchCondition);
    }

    @Override
    public AdminOrderDetailResponse getAdminOrderDetail(String salesOrderId) {
        AdminOrderDetailResponse orderDetail = salesOrderMapper.findAdminOrderDetailBySalesOrderId(salesOrderId);
        if (orderDetail == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        return orderDetail;
    }
}