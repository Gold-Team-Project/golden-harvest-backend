package com.teamgold.goldenharvest.domain.master.command.application.service.master;

import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataAppendRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataUpdatedRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.response.master.MasterResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MasterDataService {
    //마스터데이터 정보 등록
    void saveAll(List<MasterResponse> responses);
    //마스터 데이터 추가 등록
    void appendMasterData(String itemCode,MasterDataAppendRequest request);
    //마스터 데이터 비활성화
    void updateMasterDataStatus(String itemCode);
    //마스터 데이터 수정
    void updatedMasterData(String itemCode,MasterDataUpdatedRequest request);



}
