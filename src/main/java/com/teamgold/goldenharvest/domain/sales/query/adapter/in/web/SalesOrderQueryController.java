package com.teamgold.goldenharvest.domain.sales.query.adapter.in.web;

import com.teamgold.goldenharvest.domain.sales.query.application.dto.response.MyOrderResponse;
import com.teamgold.goldenharvest.domain.sales.query.application.service.SalesOrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderQueryController {
    private final SalesOrderQueryService salesOrderQueryService;

    // TRX-015 주문 리스트 조회 (사용자)
    @GetMapping
    public ResponseEntity<List<MyOrderResponse>> getMyOrders(/*@AuthenticationPrincipal UserPrincipal userPrincipal*/) {
        // 지금은 테스트를 위해 임시 이메일을 사용
        String userEmail = "test@example.com";

        // 사용자 주문 리스트 내역
        List<MyOrderResponse> myOrders = salesOrderQueryService.getMyOrders(userEmail);

        return ResponseEntity.ok(myOrders);
    }
}
