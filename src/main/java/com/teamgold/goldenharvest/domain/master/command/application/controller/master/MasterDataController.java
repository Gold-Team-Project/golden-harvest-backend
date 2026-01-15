package com.teamgold.goldenharvest.domain.master.command.application.controller.master;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataAppendRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataUpdatedRequest;
import com.teamgold.goldenharvest.domain.master.command.application.service.master.MasterDataService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/master-data")
public class MasterDataController {
    private final MasterDataService masterDataService;

    @PostMapping("/{itemCode}")
    public ResponseEntity<ApiResponse<?>> appendMasterData(
            @PathVariable String itemCode,
            @RequestBody MasterDataAppendRequest request
    ) {
        masterDataService.appendMasterData(itemCode, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @PutMapping("/{itemCode}/status")
    public ResponseEntity<ApiResponse<?>> updateMasterDataStatus(
            @PathVariable String itemCode
    ) {
        masterDataService.updateMasterDataStatus(itemCode);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{itemCode}")
    public ResponseEntity<ApiResponse<?>> updatedMasterData(
            @PathVariable String itemCode,
            @RequestBody MasterDataUpdatedRequest request
    ) {
        masterDataService.updatedMasterData(itemCode, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
