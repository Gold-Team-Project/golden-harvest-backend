package com.teamgold.goldenharvest.domain.customersupport.command.application.controller;


import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.comment.CommentCreateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.inquiry.InquiryCreateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.inquiry.InquiryUpdateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> save(
            @RequestPart("request") InquiryCreateRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        String userId = "rrrr@naver.com";
        inquiryService.create(userId, request, file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable String inquiryId) {
        //todo 인증/인가 구현 후 수정
        String userId = "rrrr@naver.com";
        inquiryService.delete(userId, inquiryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping(
            value = "/{inquiryId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable String inquiryId,
            @RequestPart("request") InquiryUpdateRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        String userId = "rrrr@naver.com";
        inquiryService.update(userId, inquiryId, request, file);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    @PostMapping("/{inquiryId}/comments")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> comment(
            @PathVariable String inquiryId,
            @RequestBody CommentCreateRequest request) {
        inquiryService.comment(inquiryId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
