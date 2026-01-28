package com.teamgold.goldenharvest.domain.sales.query.application.service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderDetailResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderHistoryResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderSearchCondition;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.MyOrderSearchCondition;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.OrderHistoryResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.mapper.SalesOrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq; // 이 import는 필요합니다.
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SalesOrderQueryServiceTest {

    @InjectMocks
    private SalesOrderQueryServiceImpl salesOrderQueryService;

    @Mock
    private SalesOrderMapper salesOrderMapper;

    private String userEmail;
    private String salesOrderId;
    private MyOrderSearchCondition myOrderSearchCondition;
    private AdminOrderSearchCondition adminOrderSearchCondition;

    @BeforeEach
    void setUp() {
        userEmail = "testuser@example.com";
        salesOrderId = "order123";
        myOrderSearchCondition = new MyOrderSearchCondition();
        adminOrderSearchCondition = new AdminOrderSearchCondition();
    }

    // --- getMyOrderHistory 테스트 ---
    @Test
    @DisplayName("내 주문 내역 조회 성공 - 검색 조건 없음")
    void testGetMyOrderHistory_NoSearchCondition_Success() {
        // Given (준비)
        List<OrderHistoryResponse> expectedList = Arrays.asList(
                OrderHistoryResponse.builder().salesOrderId("order1").build(),
                OrderHistoryResponse.builder().salesOrderId("order2").build()
        );
        // userEmail은 eq()로, searchCondition은 any()로 매칭합니다.
        given(salesOrderMapper.findOrderHistoryByUserEmail(eq(userEmail), any(MyOrderSearchCondition.class)))
                .willReturn(expectedList);

        // When (실행)
        List<OrderHistoryResponse> actualList = salesOrderQueryService.getMyOrderHistory(userEmail, myOrderSearchCondition);

        // Then (검증)
        assertThat(actualList).isEqualTo(expectedList);
        verify(salesOrderMapper, times(1)).findOrderHistoryByUserEmail(eq(userEmail), any(MyOrderSearchCondition.class));
    }

    @Test
    @DisplayName("내 주문 내역 조회 성공 - 검색 조건 포함")
    void testGetMyOrderHistory_WithSearchCondition_Success() {
        // Given (준비)
        myOrderSearchCondition.setStartDate("2023-01-01");
        myOrderSearchCondition.setEndDate("2023-12-31");
        List<OrderHistoryResponse> expectedList = Arrays.asList(
                OrderHistoryResponse.builder().salesOrderId("order3").build()
        );
        // userEmail과 searchCondition 모두 eq()로 매칭합니다.
        given(salesOrderMapper.findOrderHistoryByUserEmail(eq(userEmail), eq(myOrderSearchCondition)))
                .willReturn(expectedList);

        // When (실행)
        List<OrderHistoryResponse> actualList = salesOrderQueryService.getMyOrderHistory(userEmail, myOrderSearchCondition);

        // Then (검증)
        assertThat(actualList).isEqualTo(expectedList);
        verify(salesOrderMapper, times(1)).findOrderHistoryByUserEmail(eq(userEmail), eq(myOrderSearchCondition));
    }

    @Test
    @DisplayName("내 주문 내역 조회 성공 - 결과 없음")
    void testGetMyOrderHistory_NoResults_Success() {
        // Given (준비)
        // userEmail은 eq()로, searchCondition은 any()로 매칭합니다.
        given(salesOrderMapper.findOrderHistoryByUserEmail(eq(userEmail), any(MyOrderSearchCondition.class)))
                .willReturn(Collections.emptyList());

        // When (실행)
        List<OrderHistoryResponse> actualList = salesOrderQueryService.getMyOrderHistory(userEmail, myOrderSearchCondition);

        // Then (검증)
        assertThat(actualList).isEmpty();
        verify(salesOrderMapper, times(1)).findOrderHistoryByUserEmail(eq(userEmail), any(MyOrderSearchCondition.class));
    }

    // --- getOrderDetail 테스트 ---
    @Test
    @DisplayName("단일 주문 상세 조회 성공")
    void testGetOrderDetail_Success() {
        // Given (준비)
        OrderHistoryResponse expectedDetail = OrderHistoryResponse.builder().salesOrderId(salesOrderId).build();
        given(salesOrderMapper.findOrderDetailBySalesOrderId(salesOrderId)).willReturn(expectedDetail);

        // When (실행)
        OrderHistoryResponse actualDetail = salesOrderQueryService.getOrderDetail(salesOrderId);

        // Then (검증)
        assertThat(actualDetail).isEqualTo(expectedDetail);
        verify(salesOrderMapper, times(1)).findOrderDetailBySalesOrderId(salesOrderId);
    }

    @Test
    @DisplayName("단일 주문 상세 조회 실패 - 주문을 찾을 수 없음")
    void testGetOrderDetail_OrderNotFound_ThrowsException() {
        // Given (준비)
        given(salesOrderMapper.findOrderDetailBySalesOrderId(salesOrderId)).willReturn(null);

        // When (실행) & Then (검증)
        assertThatThrownBy(() -> salesOrderQueryService.getOrderDetail(salesOrderId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ORDER_NOT_FOUND);

        verify(salesOrderMapper, times(1)).findOrderDetailBySalesOrderId(salesOrderId);
    }

    // --- getAllOrderHistory 테스트 ---
    @Test
    @DisplayName("관리자 전체 주문 내역 조회 성공 - 검색 조건 없음")
    void testGetAllOrderHistory_NoSearchCondition_Success() {
        // Given (준비)
        List<AdminOrderHistoryResponse> expectedList = Arrays.asList(
                AdminOrderHistoryResponse.builder().salesOrderId("adminOrder1").build()
        );
        given(salesOrderMapper.findAllOrderHistory(any(AdminOrderSearchCondition.class)))
                .willReturn(expectedList);

        // When (실행)
        List<AdminOrderHistoryResponse> actualList = salesOrderQueryService.getAllOrderHistory(adminOrderSearchCondition);

        // Then (검증)
        assertThat(actualList).isEqualTo(expectedList);
        verify(salesOrderMapper, times(1)).findAllOrderHistory(any(AdminOrderSearchCondition.class));
    }

    @Test
    @DisplayName("관리자 전체 주문 내역 조회 성공 - 검색 조건 포함")
    void testGetAllOrderHistory_WithSearchCondition_Success() {
        // Given (준비)
        adminOrderSearchCondition.setStartDate(LocalDate.parse("2023-01-01")); // String을 LocalDate로 변경
        adminOrderSearchCondition.setEndDate(LocalDate.parse("2023-12-31")); // String을 LocalDate로 변경
        List<AdminOrderHistoryResponse> expectedList = Arrays.asList(
                AdminOrderHistoryResponse.builder().salesOrderId("adminOrder2").build()
        );
        given(salesOrderMapper.findAllOrderHistory(eq(adminOrderSearchCondition)))
                .willReturn(expectedList);

        // When (실행)
        List<AdminOrderHistoryResponse> actualList = salesOrderQueryService.getAllOrderHistory(adminOrderSearchCondition);

        // Then (검증)
        assertThat(actualList).isEqualTo(expectedList);
        verify(salesOrderMapper, times(1)).findAllOrderHistory(eq(adminOrderSearchCondition));
    }

    @Test
    @DisplayName("관리자 전체 주문 내역 조회 성공 - 결과 없음")
    void testGetAllOrderHistory_NoResults_Success() {
        // Given (준비)
        given(salesOrderMapper.findAllOrderHistory(any(AdminOrderSearchCondition.class)))
                .willReturn(Collections.emptyList());

        // When (실행)
        List<AdminOrderHistoryResponse> actualList = salesOrderQueryService.getAllOrderHistory(adminOrderSearchCondition);

        // Then (검증)
        assertThat(actualList).isEmpty();
        verify(salesOrderMapper, times(1)).findAllOrderHistory(any(AdminOrderSearchCondition.class));
    }

    // --- getAdminOrderDetail 테스트 ---
    @Test
    @DisplayName("관리자 단일 주문 상세 조회 성공")
    void testGetAdminOrderDetail_Success() {
        // Given (준비)
        AdminOrderDetailResponse expectedDetail = AdminOrderDetailResponse.builder().salesOrderId(salesOrderId).build();
        given(salesOrderMapper.findAdminOrderDetailBySalesOrderId(salesOrderId)).willReturn(expectedDetail);

        // When (실행)
        AdminOrderDetailResponse actualDetail = salesOrderQueryService.getAdminOrderDetail(salesOrderId);

        // Then (검증)
        assertThat(actualDetail).isEqualTo(expectedDetail);
        verify(salesOrderMapper, times(1)).findAdminOrderDetailBySalesOrderId(salesOrderId);
    }

    @Test
    @DisplayName("관리자 단일 주문 상세 조회 실패 - 주문을 찾을 수 없음")
    void testGetAdminOrderDetail_OrderNotFound_ThrowsException() {
        // Given (준비)
        given(salesOrderMapper.findAdminOrderDetailBySalesOrderId(salesOrderId)).willReturn(null);

        // When (실행) & Then (검증)
        assertThatThrownBy(() -> salesOrderQueryService.getAdminOrderDetail(salesOrderId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ORDER_NOT_FOUND);

        verify(salesOrderMapper, times(1)).findAdminOrderDetailBySalesOrderId(salesOrderId);
    }
}