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

    public void collect(PriceRequest request) {


        String response = harvestClient.callPrice(request);
        List<PriceResponse> prices = harvestParse.parsePrice(response);

        if (prices.isEmpty()) {
            log.info("원가 데이터 없음");
            return;
        }

        originPriceService.save(prices);
    }

}
