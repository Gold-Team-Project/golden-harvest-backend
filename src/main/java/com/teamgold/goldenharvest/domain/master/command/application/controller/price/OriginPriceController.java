package com.teamgold.goldenharvest.domain.master.command.application.controller.price;

import com.teamgold.goldenharvest.common.infra.harvest.collector.PriceCollector;
import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.price.PriceRequest;
import com.teamgold.goldenharvest.domain.master.command.application.service.price.OriginPriceService;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Sku;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.SkuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/harvest")
@Slf4j
public class OriginPriceController {

    private final PriceCollector priceCollector;
    private final OriginPriceService originPriceService;

    @PostMapping("/origin-price")
    public ResponseEntity<Void> collectOriginPrice() {
        //todo 하드 코딩 수정
        PriceRequest request = PriceRequest.builder()
                .product_cls_code("02")
                .item_category_code("400")
                .p_country_code("1101")
                .p_regday(LocalDate.now().minusDays(1).toString())
                .build();

        priceCollector.collect(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/origin-price/distribute")
    public ResponseEntity<ApiResponse<?>> invokeOriginPriceEvent() {
        originPriceService.publishAllOriginPriceEvent();

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
