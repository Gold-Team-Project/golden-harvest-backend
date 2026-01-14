package com.teamgold.goldenharvest.domain.customersupport.command.application.controller;


import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.comment.CommentCreateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.inquiry.InquiryCreateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.inquiry.InquiryUpdateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    @PostMapping("/{salesOrderId}")
    public ResponseEntity<ApiResponse<?>> save(
            @PathVariable String salesOrderId,
            @RequestBody InquiryCreateRequest request) {
        //todo 인증/인가 구현 후 수정
        String userId = "rrrr@naver.com";
        inquiryService.create(userId, salesOrderId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable String inquiryId) {
        inquiryService.delete(inquiryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable String inquiryId,
            @RequestBody InquiryUpdateRequest request) {
        inquiryService.update(inquiryId,request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{inquiryId}/comments")
    public ResponseEntity<ApiResponse<?>> comment(
            @PathVariable String inquiryId,
            @RequestBody CommentCreateRequest request) {
        inquiryService.comment(inquiryId,request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
