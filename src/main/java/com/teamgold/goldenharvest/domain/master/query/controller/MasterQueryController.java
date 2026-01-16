package com.teamgold.goldenharvest.domain.master.query.controller;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.master.query.service.MasterQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/master-data")
public class MasterQueryController {
    private final MasterQueryService masterQueryService;

    @GetMapping("/items")
    public ResponseEntity<ApiResponse<?>> getAllMasterData(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size){

        return ResponseEntity.ok(ApiResponse.success(masterQueryService.getAllMasterData(page,size)));
    }

    @GetMapping("/items/{skuNo}")
    public ResponseEntity<ApiResponse<?>> getDetailMasterData(@PathVariable String skuNo){

        return ResponseEntity.ok(ApiResponse.success(masterQueryService.getDetailMasterData(skuNo)));
    }

}
