package com.teamgold.goldenharvest.domain.sales.command.application.service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrder;
import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrderStatus;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.sales_order.SalesOrderRepository;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.sales_order.SalesOrderStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesOrderCommandServiceImpl implements SalesOrderCommandService {

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderStatusRepository salesOrderStatusRepository;

    private static final Long ORDER_RECEIVED_STATUS_ID = 1L;
    private static final Long PREPARING_STATUS_ID = 3L;
    private static final Long CANCELLED_STATUS_ID = 6L;

    @Override
    public void cancelOrder(String salesOrderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        // 이미 취소된 주문인지 확인
        if (salesOrder.getOrderStatus().getSalesStatusId().equals(CANCELLED_STATUS_ID)) {
            throw new BusinessException(ErrorCode.ORDER_ALREADY_CANCELLED, "이미 취소된 주문입니다.");
        }

        SalesOrderStatus cancelledStatus = salesOrderStatusRepository.findById(CANCELLED_STATUS_ID)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_STATUS_NOT_FOUND));

        salesOrder.updateStatus(cancelledStatus);

        salesOrderRepository.save(salesOrder);
    }

    @Override
    public void approveOrder(String salesOrderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        // "주문 접수" 상태일 때만 승인 가능
        if (!salesOrder.getOrderStatus().getSalesStatusId().equals(ORDER_RECEIVED_STATUS_ID)) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS, "주문 접수 상태에서만 승인이 가능합니다.");
        }

        SalesOrderStatus preparingStatus = salesOrderStatusRepository.findById(PREPARING_STATUS_ID)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_STATUS_NOT_FOUND));

        salesOrder.updateStatus(preparingStatus);

        salesOrderRepository.save(salesOrder);
    }
}
