package com.teamgold.goldenharvest.domain.master.query.service;

import com.teamgold.goldenharvest.domain.master.query.dto.response.master.MasterDataDetailResponse;
import com.teamgold.goldenharvest.domain.master.query.dto.response.master.MasterDataListResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MasterQueryService {
    // 마스터 데이터 목록 조회
    List<MasterDataListResponse> getAllMasterData(
            Integer page,
            Integer size,
            String itemName,
            String itemCode,
            String varietyName,
            String gradeName,
            Boolean isActive);
    // 마스터 데이터 상세 조회
    MasterDataDetailResponse getDetailMasterData(String skuNo);
}
