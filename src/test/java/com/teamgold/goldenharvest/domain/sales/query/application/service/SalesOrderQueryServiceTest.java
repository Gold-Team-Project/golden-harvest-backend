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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
    private Pageable pageable; // Pageable Mock 추가

    @BeforeEach
    void setUp() {
        userEmail = "testuser@example.com";
        salesOrderId = "order123";
        myOrderSearchCondition = new MyOrderSearchCondition();
        adminOrderSearchCondition = new AdminOrderSearchCondition();
        pageable = PageRequest.of(0, 10); // 기본 Pageable 객체 생성 (페이지 0, 사이즈 10)
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
        long totalCount = expectedList.size();

        given(salesOrderMapper.countOrderHistoryByUserEmail(eq(userEmail), any(MyOrderSearchCondition.class)))
                .willReturn(totalCount);
        given(salesOrderMapper.findOrderHistoryByUserEmail(eq(userEmail), any(MyOrderSearchCondition.class), eq(pageable)))
                .willReturn(expectedList);

        // When (실행)
        Page<OrderHistoryResponse> actualPage = salesOrderQueryService.getMyOrderHistory(userEmail, myOrderSearchCondition, pageable);

        // Then (검증)
        assertThat(actualPage.getContent()).isEqualTo(expectedList);
        assertThat(actualPage.getTotalElements()).isEqualTo(totalCount);
        assertThat(actualPage.getNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(actualPage.getSize()).isEqualTo(pageable.getPageSize());
        verify(salesOrderMapper, times(1)).countOrderHistoryByUserEmail(eq(userEmail), any(MyOrderSearchCondition.class));
        verify(salesOrderMapper, times(1)).findOrderHistoryByUserEmail(eq(userEmail), any(MyOrderSearchCondition.class), eq(pageable));
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
        long totalCount = expectedList.size();

        given(salesOrderMapper.countOrderHistoryByUserEmail(eq(userEmail), eq(myOrderSearchCondition)))
                .willReturn(totalCount);
        given(salesOrderMapper.findOrderHistoryByUserEmail(eq(userEmail), eq(myOrderSearchCondition), eq(pageable)))
                .willReturn(expectedList);

        // When (실행)
        Page<OrderHistoryResponse> actualPage = salesOrderQueryService.getMyOrderHistory(userEmail, myOrderSearchCondition, pageable);

        // Then (검증)
        assertThat(actualPage.getContent()).isEqualTo(expectedList);
        assertThat(actualPage.getTotalElements()).isEqualTo(totalCount);
        assertThat(actualPage.getNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(actualPage.getSize()).isEqualTo(pageable.getPageSize());
        verify(salesOrderMapper, times(1)).countOrderHistoryByUserEmail(eq(userEmail), eq(myOrderSearchCondition));
        verify(salesOrderMapper, times(1)).findOrderHistoryByUserEmail(eq(userEmail), eq(myOrderSearchCondition), eq(pageable));
    }

    @Test
    @DisplayName("내 주문 내역 조회 성공 - 결과 없음")
    void testGetMyOrderHistory_NoResults_Success() {
        // Given (준비)
        long totalCount = 0;
        given(salesOrderMapper.countOrderHistoryByUserEmail(eq(userEmail), any(MyOrderSearchCondition.class)))
                .willReturn(totalCount);
        given(salesOrderMapper.findOrderHistoryByUserEmail(eq(userEmail), any(MyOrderSearchCondition.class), eq(pageable)))
                .willReturn(Collections.emptyList());

        // When (실행)
        Page<OrderHistoryResponse> actualPage = salesOrderQueryService.getMyOrderHistory(userEmail, myOrderSearchCondition, pageable);

        // Then (검증)
        assertThat(actualPage.getContent()).isEmpty();
        assertThat(actualPage.getTotalElements()).isEqualTo(totalCount);
        assertThat(actualPage.getNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(actualPage.getSize()).isEqualTo(pageable.getPageSize());
        verify(salesOrderMapper, times(1)).countOrderHistoryByUserEmail(eq(userEmail), any(MyOrderSearchCondition.class));
        verify(salesOrderMapper, times(1)).findOrderHistoryByUserEmail(eq(userEmail), any(MyOrderSearchCondition.class), eq(pageable));
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
        long totalCount = expectedList.size();

        given(salesOrderMapper.countAllOrderHistory(any(AdminOrderSearchCondition.class)))
                .willReturn(totalCount);
        given(salesOrderMapper.findAllOrderHistory(any(AdminOrderSearchCondition.class), eq(pageable)))
                .willReturn(expectedList);

        // When (실행)
        Page<AdminOrderHistoryResponse> actualPage = salesOrderQueryService.getAllOrderHistory(adminOrderSearchCondition, pageable);

        // Then (검증)
        assertThat(actualPage.getContent()).isEqualTo(expectedList);
        assertThat(actualPage.getTotalElements()).isEqualTo(totalCount);
        assertThat(actualPage.getNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(actualPage.getSize()).isEqualTo(pageable.getPageSize());
        verify(salesOrderMapper, times(1)).countAllOrderHistory(any(AdminOrderSearchCondition.class));
        verify(salesOrderMapper, times(1)).findAllOrderHistory(any(AdminOrderSearchCondition.class), eq(pageable));
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
        long totalCount = expectedList.size();

        given(salesOrderMapper.countAllOrderHistory(eq(adminOrderSearchCondition)))
                .willReturn(totalCount);
        given(salesOrderMapper.findAllOrderHistory(eq(adminOrderSearchCondition), eq(pageable)))
                .willReturn(expectedList);

        // When (실행)
        Page<AdminOrderHistoryResponse> actualPage = salesOrderQueryService.getAllOrderHistory(adminOrderSearchCondition, pageable);

        // Then (검증)
        assertThat(actualPage.getContent()).isEqualTo(expectedList);
        assertThat(actualPage.getTotalElements()).isEqualTo(totalCount);
        assertThat(actualPage.getNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(actualPage.getSize()).isEqualTo(pageable.getPageSize());
        verify(salesOrderMapper, times(1)).countAllOrderHistory(eq(adminOrderSearchCondition));
        verify(salesOrderMapper, times(1)).findAllOrderHistory(eq(adminOrderSearchCondition), eq(pageable));
    }

    @Test
    @DisplayName("관리자 전체 주문 내역 조회 성공 - 결과 없음")
    void testGetAllOrderHistory_NoResults_Success() {
        // Given (준비)
        long totalCount = 0;
        given(salesOrderMapper.countAllOrderHistory(any(AdminOrderSearchCondition.class)))
                .willReturn(totalCount);
        given(salesOrderMapper.findAllOrderHistory(any(AdminOrderSearchCondition.class), eq(pageable)))
                .willReturn(Collections.emptyList());

        // When (실행)
        Page<AdminOrderHistoryResponse> actualPage = salesOrderQueryService.getAllOrderHistory(adminOrderSearchCondition, pageable);

        // Then (검증)
        assertThat(actualPage.getContent()).isEmpty();
        assertThat(actualPage.getTotalElements()).isEqualTo(totalCount);
        assertThat(actualPage.getNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(actualPage.getSize()).isEqualTo(pageable.getPageSize());
        verify(salesOrderMapper, times(1)).countAllOrderHistory(any(AdminOrderSearchCondition.class));
        verify(salesOrderMapper, times(1)).findAllOrderHistory(any(AdminOrderSearchCondition.class), eq(pageable));
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
    // --- 날짜 엣지 케이스 테스트 ---
    @Test
    @DisplayName("내 주문 내역 조회 - 시작일이 종료일보다 늦을 경우 빈 페이지 반환")
    void testGetMyOrderHistory_StartDateAfterEndDate_ReturnsEmptyPage() {
        // Given (준비)
        myOrderSearchCondition.setStartDate("2023-01-31");
        myOrderSearchCondition.setEndDate("2023-01-01"); // 시작일 > 종료일

        long totalCount = 0;
        given(salesOrderMapper.countOrderHistoryByUserEmail(eq(userEmail), eq(myOrderSearchCondition)))
                .willReturn(totalCount);
        given(salesOrderMapper.findOrderHistoryByUserEmail(eq(userEmail), eq(myOrderSearchCondition), eq(pageable)))
                .willReturn(Collections.emptyList());

        // When (실행)
        Page<OrderHistoryResponse> actualPage = salesOrderQueryService.getMyOrderHistory(userEmail, myOrderSearchCondition, pageable);

        // Then (검증)
        assertThat(actualPage.getContent()).isEmpty();
        assertThat(actualPage.getTotalElements()).isEqualTo(totalCount);
        verify(salesOrderMapper, times(1)).countOrderHistoryByUserEmail(eq(userEmail), eq(myOrderSearchCondition));
        verify(salesOrderMapper, times(1)).findOrderHistoryByUserEmail(eq(userEmail), eq(myOrderSearchCondition), eq(pageable));
    }

    @Test
    @DisplayName("관리자 전체 주문 내역 조회 - 시작일이 종료일보다 늦을 경우 빈 페이지 반환")
    void testGetAllOrderHistory_StartDateAfterEndDate_ReturnsEmptyPage() {
        // Given (준비)
        adminOrderSearchCondition.setStartDate(LocalDate.parse("2023-01-31"));
        adminOrderSearchCondition.setEndDate(LocalDate.parse("2023-01-01")); // 시작일 > 종료일

        long totalCount = 0;
        given(salesOrderMapper.countAllOrderHistory(eq(adminOrderSearchCondition)))
                .willReturn(totalCount);
        given(salesOrderMapper.findAllOrderHistory(eq(adminOrderSearchCondition), eq(pageable)))
                .willReturn(Collections.emptyList());

        // When (실행)
        Page<AdminOrderHistoryResponse> actualPage = salesOrderQueryService.getAllOrderHistory(adminOrderSearchCondition, pageable);

        // Then (검증)
        assertThat(actualPage.getContent()).isEmpty();
        assertThat(actualPage.getTotalElements()).isEqualTo(totalCount);
        verify(salesOrderMapper, times(1)).countAllOrderHistory(eq(adminOrderSearchCondition));
        verify(salesOrderMapper, times(1)).findAllOrderHistory(eq(adminOrderSearchCondition), eq(pageable));
    }

    // --- 관리자 검색 조건 조합 테스트 ---
    @Test
    @DisplayName("관리자 전체 주문 내역 조회 - 고객 회사명으로 검색 성공")
    void testGetAllOrderHistory_SearchByCustomerName_Success() {
        // Given (준비)
        adminOrderSearchCondition.setCustomerName("골든하베스트");
        List<AdminOrderHistoryResponse> expectedList = Arrays.asList(
                AdminOrderHistoryResponse.builder().salesOrderId("adminOrder1").build()
        );
        long totalCount = expectedList.size();

        given(salesOrderMapper.countAllOrderHistory(eq(adminOrderSearchCondition)))
                .willReturn(totalCount);
        given(salesOrderMapper.findAllOrderHistory(eq(adminOrderSearchCondition), eq(pageable)))
                .willReturn(expectedList);

        // When (실행)
        Page<AdminOrderHistoryResponse> actualPage = salesOrderQueryService.getAllOrderHistory(adminOrderSearchCondition, pageable);

        // Then (검증)
        assertThat(actualPage.getContent()).isEqualTo(expectedList);
        assertThat(actualPage.getTotalElements()).isEqualTo(totalCount);
        verify(salesOrderMapper, times(1)).countAllOrderHistory(eq(adminOrderSearchCondition));
        verify(salesOrderMapper, times(1)).findAllOrderHistory(eq(adminOrderSearchCondition), eq(pageable));
    }

    @Test
    @DisplayName("관리자 전체 주문 내역 조회 - 주문 상태로 검색 성공")
    void testGetAllOrderHistory_SearchByOrderStatus_Success() {
        // Given (준비)
        adminOrderSearchCondition.setOrderStatus("PENDING");
        List<AdminOrderHistoryResponse> expectedList = Arrays.asList(
                AdminOrderHistoryResponse.builder().salesOrderId("adminOrder3").build()
        );
        long totalCount = expectedList.size();

        given(salesOrderMapper.countAllOrderHistory(eq(adminOrderSearchCondition)))
                .willReturn(totalCount);
        given(salesOrderMapper.findAllOrderHistory(eq(adminOrderSearchCondition), eq(pageable)))
                .willReturn(expectedList);

        // When (실행)
        Page<AdminOrderHistoryResponse> actualPage = salesOrderQueryService.getAllOrderHistory(adminOrderSearchCondition, pageable);

        // Then (검증)
        assertThat(actualPage.getContent()).isEqualTo(expectedList);
        assertThat(actualPage.getTotalElements()).isEqualTo(totalCount);
        verify(salesOrderMapper, times(1)).countAllOrderHistory(eq(adminOrderSearchCondition));
        verify(salesOrderMapper, times(1)).findAllOrderHistory(eq(adminOrderSearchCondition), eq(pageable));
    }

    @Test
    @DisplayName("관리자 전체 주문 내역 조회 - 고객 회사명과 주문 상태 조합 검색 성공")
    void testGetAllOrderHistory_SearchCombination_Success() {
        // Given (준비)
        adminOrderSearchCondition.setCustomerName("특정회사");
        adminOrderSearchCondition.setOrderStatus("DELIVERED");
        List<AdminOrderHistoryResponse> expectedList = Arrays.asList(
                AdminOrderHistoryResponse.builder().salesOrderId("adminOrder4").build()
        );
        long totalCount = expectedList.size();

        given(salesOrderMapper.countAllOrderHistory(eq(adminOrderSearchCondition)))
                .willReturn(totalCount);
        given(salesOrderMapper.findAllOrderHistory(eq(adminOrderSearchCondition), eq(pageable)))
                .willReturn(expectedList);

        // When (실행)
        Page<AdminOrderHistoryResponse> actualPage = salesOrderQueryService.getAllOrderHistory(adminOrderSearchCondition, pageable);

        // Then (검증)
        assertThat(actualPage.getContent()).isEqualTo(expectedList);
        assertThat(actualPage.getTotalElements()).isEqualTo(totalCount);
        verify(salesOrderMapper, times(1)).countAllOrderHistory(eq(adminOrderSearchCondition));
        verify(salesOrderMapper, times(1)).findAllOrderHistory(eq(adminOrderSearchCondition), eq(pageable));
    }
}