package com.teamgold.goldenharvest.domain.master.query.service.impl;

import com.teamgold.goldenharvest.domain.master.query.dto.response.master.MasterDataDetailResponse;
import com.teamgold.goldenharvest.domain.master.query.dto.response.master.MasterDataListResponse;
import com.teamgold.goldenharvest.domain.master.query.dto.response.price.OriginPriceResponse;
import com.teamgold.goldenharvest.domain.master.query.mapper.MasterMapper;
import com.teamgold.goldenharvest.domain.master.query.service.MasterQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class MasterQueryServiceImpl implements MasterQueryService {
    private final MasterMapper masterMapper;
    @Override
    public List<MasterDataListResponse> getAllMasterData(
            Integer page,
            Integer size,
            String itemName,
            String itemCode,
            String varietyName,
            String gradeName,
            Boolean isActive) {
        int limit = size;
        int offset = (page-1) * limit;
        return masterMapper.findAllMasterData(itemName, itemCode,varietyName, gradeName, isActive, limit, offset);
    }

    @Override
    public MasterDataDetailResponse getDetailMasterData(String skuNo) {

        MasterDataDetailResponse masterData =
                masterMapper.findDetailMasterData(skuNo);

        List<OriginPriceResponse> prices =
                masterMapper.findRecentOriginPrices(skuNo);


        log.info("가격들 {}", prices.toString());
        masterData.setOriginPrices(prices);
        return masterData;
    }

}
