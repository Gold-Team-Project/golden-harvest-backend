package com.teamgold.goldenharvest.domain.sales.command.application.service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrder;
import com.teamgold.goldenharvest.domain.sales.command.domain.sales_order.SalesOrderStatus;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.sales_order.SalesOrderRepository;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.sales_order.SalesOrderStatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SalesOrderCommandServiceTest {

    @InjectMocks
    private SalesOrderCommandServiceImpl salesOrderCommandService;

    @Mock
    private SalesOrderRepository salesOrderRepository;

    @Mock
    private SalesOrderStatusRepository salesOrderStatusRepository;

    private static final String TEST_SALES_ORDER_ID = "test-order-id";
    private static final Long ORDER_RECEIVED_STATUS_ID = 1L; // 주문 접수
    private static final Long PREPARING_STATUS_ID = 3L;      // 배송 준비중
    private static final Long CANCELLED_STATUS_ID = 6L;     // 주문 취소

    @Test
    @DisplayName("주문 취소 성공 - 어떤 상태에서도 취소 가능")
    void testCancelOrder_Success() {
        // Given
        SalesOrderStatus currentStatus = new SalesOrderStatus(PREPARING_STATUS_ID, "배송 준비중", "PREPARING");
        SalesOrder salesOrder = SalesOrder.builder()
                .salesOrderId(TEST_SALES_ORDER_ID)
                .orderStatus(currentStatus)
                .build();
        SalesOrderStatus cancelledStatus = new SalesOrderStatus(CANCELLED_STATUS_ID, "주문 취소", "CANCELLED");

        given(salesOrderRepository.findById(TEST_SALES_ORDER_ID)).willReturn(Optional.of(salesOrder));
        given(salesOrderStatusRepository.findById(CANCELLED_STATUS_ID)).willReturn(Optional.of(cancelledStatus));

        // When
        salesOrderCommandService.cancelOrder(TEST_SALES_ORDER_ID);

        // Then
        verify(salesOrderRepository, times(1)).findById(TEST_SALES_ORDER_ID);
        verify(salesOrderStatusRepository, times(1)).findById(CANCELLED_STATUS_ID);
        verify(salesOrderRepository, times(1)).save(any(SalesOrder.class));
    }

    @Test
    @DisplayName("주문 취소 실패 - 주문을 찾을 수 없음")
    void testCancelOrder_OrderNotFound_ThrowsException() {
        // Given
        given(salesOrderRepository.findById(TEST_SALES_ORDER_ID)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> salesOrderCommandService.cancelOrder(TEST_SALES_ORDER_ID))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ORDER_NOT_FOUND);

        verify(salesOrderRepository, times(1)).findById(TEST_SALES_ORDER_ID);
        verify(salesOrderStatusRepository, times(0)).findById(any()); // never()를 times(0)으로 변경
        verify(salesOrderRepository, times(0)).save(any(SalesOrder.class)); // never()를 times(0)으로 변경
    }

    @Test
    @DisplayName("주문 승인 성공 - 주문 접수 상태일 때")
    void testApproveOrder_Success_WhenPending() {
        // Given
        SalesOrderStatus orderReceivedStatus = new SalesOrderStatus(ORDER_RECEIVED_STATUS_ID, "주문 접수", "PENDING");
        SalesOrder salesOrder = SalesOrder.builder()
                .salesOrderId(TEST_SALES_ORDER_ID)
                .orderStatus(orderReceivedStatus)
                .build();
        SalesOrderStatus preparingStatus = new SalesOrderStatus(PREPARING_STATUS_ID, "배송 준비중", "PREPARING");

        given(salesOrderRepository.findById(TEST_SALES_ORDER_ID)).willReturn(Optional.of(salesOrder));
        given(salesOrderStatusRepository.findById(PREPARING_STATUS_ID)).willReturn(Optional.of(preparingStatus));

        // When
        salesOrderCommandService.approveOrder(TEST_SALES_ORDER_ID);

        // Then
        verify(salesOrderRepository, times(1)).findById(TEST_SALES_ORDER_ID);
        verify(salesOrderStatusRepository, times(1)).findById(PREPARING_STATUS_ID);
        verify(salesOrderRepository, times(1)).save(any(SalesOrder.class));
    }

    @Test
    @DisplayName("주문 승인 실패 - 주문을 찾을 수 없음")
    void testApproveOrder_OrderNotFound_ThrowsException() {
        // Given
        given(salesOrderRepository.findById(TEST_SALES_ORDER_ID)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> salesOrderCommandService.approveOrder(TEST_SALES_ORDER_ID))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ORDER_NOT_FOUND);

        verify(salesOrderRepository, times(1)).findById(TEST_SALES_ORDER_ID);
        verify(salesOrderStatusRepository, times(0)).findById(any()); // never()를 times(0)으로 변경
        verify(salesOrderRepository, times(0)).save(any(SalesOrder.class)); // never()를 times(0)으로 변경
    }

    @Test
    @DisplayName("주문 승인 실패 - 주문 접수 상태가 아님")
    void testApproveOrder_InvalidStatus_ThrowsException() {
        // Given
        SalesOrderStatus notPendingStatus = new SalesOrderStatus(PREPARING_STATUS_ID, "배송 준비중", "PREPARING"); // Not PENDING
        SalesOrder salesOrder = new SalesOrder(
                TEST_SALES_ORDER_ID,
                "user@example.com", // userEmail
                notPendingStatus,
                null, null, null, // createdAt, updatedAt, deliveryDate
                null,             // totalAmount
                null              // salesOrderItems
        );

        given(salesOrderRepository.findById(TEST_SALES_ORDER_ID)).willReturn(Optional.of(salesOrder));

        // When & Then
        assertThatThrownBy(() -> salesOrderCommandService.approveOrder(TEST_SALES_ORDER_ID))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_ORDER_STATUS)
                .hasMessageContaining("주문 접수 상태에서만 승인이 가능합니다.");

        verify(salesOrderRepository, times(1)).findById(TEST_SALES_ORDER_ID);
        verify(salesOrderStatusRepository, times(0)).findById(any()); // never()를 times(0)으로 변경
        verify(salesOrderRepository, times(0)).save(any(SalesOrder.class)); // never()를 times(0)으로 변경
    }

    @Test
    @DisplayName("주문 취소 실패 - 이미 취소된 주문")
    void testCancelOrder_AlreadyCancelled_ThrowsException() {
        // Given (준비)
        SalesOrderStatus alreadyCancelledStatus = new SalesOrderStatus(CANCELLED_STATUS_ID, "주문 취소", "CANCELLED");
        SalesOrder salesOrder = SalesOrder.builder()
                .salesOrderId(TEST_SALES_ORDER_ID)
                .orderStatus(alreadyCancelledStatus)
                .build();

        given(salesOrderRepository.findById(TEST_SALES_ORDER_ID)).willReturn(Optional.of(salesOrder));

        // When (실행) & Then (검증)
        assertThatThrownBy(() -> salesOrderCommandService.cancelOrder(TEST_SALES_ORDER_ID))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ORDER_ALREADY_CANCELLED)
                .hasMessageContaining("이미 취소된 주문입니다.");

        verify(salesOrderRepository, times(1)).findById(TEST_SALES_ORDER_ID);
        verify(salesOrderStatusRepository, times(0)).findById(any()); // never()를 times(0)으로 변경
        verify(salesOrderRepository, times(0)).save(any(SalesOrder.class)); // never()를 times(0)으로 변경
    }
}
