package com.teamgold.goldenharvest.domain.purchases.command.application.controller;

import com.teamgold.goldenharvest.domain.purchases.command.application.service.PurchaseCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class PurchaseCommandController {

    private final PurchaseCommandService purchaseCommandService;



}
