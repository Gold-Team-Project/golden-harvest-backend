package com.teamgold.goldenharvest.domain.master.command.application.controller.master;

import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterRequest;
import com.teamgold.goldenharvest.common.infra.harvest.collector.MasterCollector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/harvest")
@Slf4j
public class MasterController {

    private final MasterCollector masterCollector;

    @PostMapping
    public ResponseEntity<Void> collectProduceMaster() {
        //todo 하드코딩 수정
        MasterRequest request = MasterRequest.builder()
                .pStartday("2025-10-28")
                .pEndday("2025-11-11")
                .pItemcategorycode("400")
                .build();

        log.info("[HARVEST] collect produce master start");

        masterCollector.collect(request);

        return ResponseEntity.ok().build();
    }
}
