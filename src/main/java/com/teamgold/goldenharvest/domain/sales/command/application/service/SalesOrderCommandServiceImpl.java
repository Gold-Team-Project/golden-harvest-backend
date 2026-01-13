package com.teamgold.goldenharvest.domain.sales.command.application.service;

import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrder;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesOrderCommandServiceImpl implements SalesOrderCommandService {
    private final SalesOrderRepository salesOrderRepository;

    @Override
    public void updateUserEmail(String userEmail, String newUserEmail) {
        List<SalesOrder> salesOrders = salesOrderRepository.findByUserEmail(userEmail);
        for (SalesOrder salesOrder : salesOrders) {
            salesOrder.updateUserEmail(newUserEmail);
        }
    }
}
