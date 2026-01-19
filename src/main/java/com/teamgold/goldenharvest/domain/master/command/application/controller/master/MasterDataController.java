package com.teamgold.goldenharvest.domain.master.command.application.controller.master;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataAppendRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataUpdatedRequest;
import com.teamgold.goldenharvest.domain.master.command.application.service.master.MasterDataService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/master-data")
public class MasterDataController {
    private final MasterDataService masterDataService;

    @PostMapping(
            value = "/{itemCode}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> appendMasterData(
            @PathVariable String itemCode,
            @RequestPart("request") MasterDataAppendRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        masterDataService.appendMasterData(itemCode, request,file);
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

    @PatchMapping(
            value = "/{itemCode}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<?>> updatedMasterData(
            @PathVariable String itemCode,
            @RequestParam("request") String requestJson,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        MasterDataUpdatedRequest request = objectMapper.readValue(requestJson, MasterDataUpdatedRequest.class);

        masterDataService.updatedMasterData(itemCode, request, file);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
