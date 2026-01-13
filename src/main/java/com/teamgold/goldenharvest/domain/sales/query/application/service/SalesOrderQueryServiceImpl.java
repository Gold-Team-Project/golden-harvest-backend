package com.teamgold.goldenharvest.domain.sales.query.application.service;

import com.teamgold.goldenharvest.domain.sales.command.infrastructure.repository.SalesOrderRepository;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.response.MyOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SalesOrderQueryServiceImpl implements SalesOrderQueryService {
    private final SalesOrderRepository salesOrderRepository;

    @Override
    public List<MyOrderResponse> getMyOrders(String userEmail) {
        return salesOrderRepository.findByUserEmailOrderByCreatedAtDesc(userEmail).stream()
                .map(MyOrderResponse::from)
                .collect(Collectors.toList());
    }
}
