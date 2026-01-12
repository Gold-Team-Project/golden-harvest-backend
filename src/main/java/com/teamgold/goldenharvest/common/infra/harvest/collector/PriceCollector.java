package com.teamgold.goldenharvest.common.infra.harvest.collector;

import com.teamgold.goldenharvest.common.infra.harvest.HarvestClient;
import com.teamgold.goldenharvest.common.infra.harvest.HarvestParse;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.price.PriceRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.response.price.PriceResponse;
import com.teamgold.goldenharvest.domain.master.command.application.service.price.OriginPriceService;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Sku;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PriceCollector {

    private final HarvestClient harvestClient;
    private final HarvestParse harvestParse;
    private final OriginPriceService originPriceService;

    public void collectBySku(Sku sku) {

        PriceRequest request = PriceRequest.builder()
                .product_cls_code("02") // 도매 기준
                .item_category_code("400")
                .p_country_code("1101") // 서울 (도매 가능 지역)
                .p_regday(LocalDate.now().toString())
                .build();

        String response = harvestClient.callPrice(request);
        List<PriceResponse> allPrices = harvestParse.parsePrice(response);

        List<PriceResponse> matched = allPrices.stream()
                .filter(p ->
                        sku.getProduceMaster().getItemCode().equals(p.getItemCode())
                                && sku.getVariety().getVarietyCode().equals(p.getKindCode())
                                && sku.getGrade().getGradeCode().equals(p.getRank())
                )
                .toList();

        if (matched.isEmpty()) {
            log.info("[HARVEST] no matched price skuNo={}", sku.getSkuNo());
            return;
        }

        originPriceService.save(sku, matched);
    }

}
