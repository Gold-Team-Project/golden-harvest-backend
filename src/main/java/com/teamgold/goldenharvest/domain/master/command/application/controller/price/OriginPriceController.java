package com.teamgold.goldenharvest.domain.master.command.application.controller.price;

import com.teamgold.goldenharvest.common.infra.harvest.collector.PriceCollector;
import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Sku;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.SkuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/harvest")
@Slf4j
public class OriginPriceController {

    private final SkuRepository skuRepository;
    private final PriceCollector priceCollector;

    @PostMapping("/origin-price")
    public ResponseEntity<Void> collectOriginPrice() {

        List<Sku> skus = skuRepository.findAll();

        log.info("[HARVEST] origin price collect start - skuCount={}", skus.size());

        for (Sku sku : skus) {
            try {
                priceCollector.collectBySku(sku);
            } catch (Exception e) {
                log.error("[HARVEST] price collect fail skuNo={}", sku.getSkuNo(), e);
            }
        }

        return ResponseEntity.ok().build();
    }
}
