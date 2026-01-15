package com.teamgold.goldenharvest.domain.master.command.application.service.price.impl;

import com.teamgold.goldenharvest.domain.master.command.application.dto.response.price.PriceResponse;
import com.teamgold.goldenharvest.domain.master.command.application.service.price.OriginPriceService;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Sku;
import com.teamgold.goldenharvest.domain.master.command.domain.price.OriginPrice;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.SkuRepository;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.price.OriginPriceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OriginPriceServiceImpl implements OriginPriceService {

    private final OriginPriceRepository originPriceRepository;
    private final SkuRepository skuRepository;

    @Override
    public void saveOriginPrice(Sku sku, PriceResponse price) {

        OriginPrice prices = OriginPrice.builder()
                .sku(sku)
                .originPrice(price.getDpr1())
                .unit(price.getUnit())
                .createdAt(LocalDate.now().minusDays(1))
                .build();

        originPriceRepository.save(prices);
    }

    @Override
    @Transactional
    public void save(List<PriceResponse> prices) {

        List<Sku> skus = skuRepository.findAll();

        for (Sku sku : skus) {
            prices.stream()
                    .filter(p ->
                            sku.getProduceMaster().getItemCode().equals(p.getItemCode())
                                    && sku.getVariety().getVarietyCode().equals(p.getKindCode())
                                    && sku.getGrade().getGradeCode().equals(p.getRank())
                    )
                    .findFirst()
                    .ifPresent(price -> saveOriginPrice(sku, price));
        }
    }
}

