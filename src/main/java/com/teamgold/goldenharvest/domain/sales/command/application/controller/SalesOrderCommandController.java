package com.teamgold.goldenharvest.domain.sales.command.application.controller;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.sales.command.application.service.SalesOrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesOrderCommandController {

    private final SalesOrderCommandService salesOrderCommandService;

    @PatchMapping("/orders/{salesOrderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable String salesOrderId) {
        salesOrderCommandService.cancelOrder(salesOrderId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/orders/{salesOrderId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveOrder(@PathVariable String salesOrderId) {
        salesOrderCommandService.approveOrder(salesOrderId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
