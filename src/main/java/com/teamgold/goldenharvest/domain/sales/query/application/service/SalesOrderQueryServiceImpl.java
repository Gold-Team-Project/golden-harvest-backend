package com.teamgold.goldenharvest.domain.sales.query.application.service;

import com.teamgold.goldenharvest.domain.sales.query.application.dto.OrderHistoryResponse;
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
    public List<OrderHistoryResponse> getMyOrderHistory(String userEmail) {
        return salesOrderMapper.findOrderHistoryByUserEmail(userEmail);
    }
}