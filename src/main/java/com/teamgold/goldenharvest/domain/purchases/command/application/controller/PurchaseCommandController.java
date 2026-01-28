package com.teamgold.goldenharvest.domain.purchases.command.application.controller;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.purchases.command.application.service.PurchaseCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class PurchaseCommandController {

    private final PurchaseCommandService purchaseCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createPurcahseOrder(
            @RequestParam Long quantity
            , @RequestParam String skuNo
    ) {
        purchaseCommandService.createPurchaseOrder(quantity, skuNo);

        return ResponseEntity.ok(ApiResponse.success(null));
    }



}
