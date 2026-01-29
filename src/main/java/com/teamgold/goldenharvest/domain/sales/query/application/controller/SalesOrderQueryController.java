package com.teamgold.goldenharvest.domain.sales.query.application.controller;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderDetailResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderHistoryResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.OrderHistoryResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.AdminOrderSearchCondition;
import com.teamgold.goldenharvest.domain.sales.query.application.dto.MyOrderSearchCondition;
import com.teamgold.goldenharvest.domain.sales.query.application.service.SalesOrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page; // Page import
import org.springframework.data.domain.Pageable; // Pageable import
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PathVariable;



import java.util.List;



@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesOrderQueryController {
    private final SalesOrderQueryService salesOrderQueryService;

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<Page<OrderHistoryResponse>>> getMyOrderHistory(@ModelAttribute MyOrderSearchCondition searchCondition, Pageable pageable) {
        // 최종 구현 시에는 Spring Security 등의 인증 시스템에서 사용자 이메일을 받아올 예정
        // userEmail 하드코딩으로 받아 옴
                String userEmail = "testuser@example.com";
                Page<OrderHistoryResponse> orderHistory = salesOrderQueryService.getMyOrderHistory(userEmail, searchCondition, pageable);
        return ResponseEntity.ok(ApiResponse.success(orderHistory));
    }

    @GetMapping("/{salesOrderId}")
    public ResponseEntity<ApiResponse<OrderHistoryResponse>> getOrderDetail(@PathVariable String salesOrderId) {
        OrderHistoryResponse orderDetail = salesOrderQueryService.getOrderDetail(salesOrderId);
        return ResponseEntity.ok(ApiResponse.success(orderDetail));
    }

    // 관리자가 사용자 주문 내역 조회하는 기능
    @GetMapping("/all-orders")
    public ResponseEntity<ApiResponse<Page<AdminOrderHistoryResponse>>> getAllOrderHistory(@ModelAttribute AdminOrderSearchCondition searchCondition, Pageable pageable) {
        Page<AdminOrderHistoryResponse> orderHistory = salesOrderQueryService.getAllOrderHistory(searchCondition, pageable);
        return ResponseEntity.ok(ApiResponse.success(orderHistory));
    }

    // 관리자용 상세 주문 내역 조회 기능
    @GetMapping("/orders/{salesOrderId}/details")
    public ResponseEntity<ApiResponse<AdminOrderDetailResponse>> getAdminOrderDetail(@PathVariable String salesOrderId) {
        AdminOrderDetailResponse orderDetail = salesOrderQueryService.getAdminOrderDetail(salesOrderId);
        return ResponseEntity.ok(ApiResponse.success(orderDetail));
    }
}
