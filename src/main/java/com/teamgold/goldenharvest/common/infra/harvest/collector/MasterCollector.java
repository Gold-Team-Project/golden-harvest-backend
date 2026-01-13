package com.teamgold.goldenharvest.common.infra.harvest.collector;

import com.teamgold.goldenharvest.common.infra.harvest.*;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.response.master.MasterResponse;
import com.teamgold.goldenharvest.domain.master.command.application.service.master.MasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor

public class MasterCollector {
    private final HarvestClient harvestClient;
    private final HarvestParse harvestParse;
    private final MasterService masterService;

    public void collect(MasterRequest request) {
        String response = harvestClient.callProduct(request);
        List<MasterResponse> masters = harvestParse.parseProduct(response);

        if(masters.isEmpty()){
            //todo 예외 처리 추가
            log.info("수집된 마스터 데이터 없음");
            return;
        }
        masterService.saveAll(masters);

    }

}

