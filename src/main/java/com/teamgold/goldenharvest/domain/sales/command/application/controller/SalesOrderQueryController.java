package com.teamgold.goldenharvest.domain.sales.command.application.controller;


import com.teamgold.goldenharvest.domain.sales.command.application.dto.response.MyOrderResponse;
import com.teamgold.goldenharvest.domain.sales.command.application.service.SalesOrderQueryService;
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

    @GetMapping
    public ResponseEntity<List<MyOrderResponse>> getMyOrders(/*@AuthenticationPrincipal UserPrincipal userPrincipal*/) {
        // [중요] 실제로는 아래와 같이 Spring Security의 Principal 객체에서 사용자 정보를 가져옵니다.
        // String userEmail = userPrincipal.getEmail();

        // 지금은 테스트를 위해 임시 이메일을 사용합니다.
        String userEmail = "test@example.com";

        List<MyOrderResponse> myOrders = salesOrderQueryService.getMyOrders(userEmail);

        return ResponseEntity.ok(myOrders);
    }
}
