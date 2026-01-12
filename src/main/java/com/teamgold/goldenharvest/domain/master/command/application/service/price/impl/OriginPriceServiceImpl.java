package com.teamgold.goldenharvest.domain.master.command.application.service.price.impl;

import com.teamgold.goldenharvest.domain.master.command.application.dto.response.price.PriceResponse;
import com.teamgold.goldenharvest.domain.master.command.application.service.price.OriginPriceService;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Sku;
import com.teamgold.goldenharvest.domain.master.command.domain.price.OriginPrice;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.price.OriginPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OriginPriceServiceImpl implements OriginPriceService {

    private final OriginPriceRepository originPriceRepository;

    @Override
    public void save(Sku sku, List<PriceResponse> responses) {

        List<OriginPrice> entities = responses.stream()
                .map(r -> OriginPrice.builder()
                        .sku(sku)
                        .originPrice(r.getDpr1())
                        .createdAt(LocalDate.now())
                        .unit(r.getUnit())
                        .build()
                )
                .toList();

        originPriceRepository.saveAll(entities);
    }
    }

