package com.teamgold.goldenharvest.domain.master.command.application.service.price;

import com.teamgold.goldenharvest.domain.master.command.application.dto.response.price.PriceResponse;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Sku;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OriginPriceService {
    //원가 정보 등록
    void saveOriginPrice(Sku sku, PriceResponse price);
    void save (List<PriceResponse> responses);
}
