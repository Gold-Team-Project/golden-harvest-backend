package com.teamgold.goldenharvest.domain.customersupport.query.controller;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.customersupport.command.application.service.InquiryService;
import com.teamgold.goldenharvest.domain.customersupport.query.service.InquiryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InquiryQueryController {
    private final InquiryQueryService inquiryQueryService;

    @GetMapping("/inquiries")
    public ResponseEntity<ApiResponse<?>> getAllInquiry(
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size) {

        return ResponseEntity.ok(ApiResponse.success(inquiryQueryService.getAllInquiry(userId, page, size)));
    }

    @GetMapping("/inquiries/{inquiryNo}")
    public ResponseEntity<ApiResponse<?>> getDetailInquiry(@PathVariable String inquiryNo) {
        return ResponseEntity.ok(ApiResponse.success(inquiryQueryService.getDetailInquiry(inquiryNo)));
    }

    @GetMapping("/admin/inquiries")
    public ResponseEntity<ApiResponse<?>> getAllAdminInquiry(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
        return ResponseEntity.ok(ApiResponse.success(inquiryQueryService.getAllAdminInquiry(page, size)));
    }

    @GetMapping("/admin/inquiries/{inquiryNo}")
    public ResponseEntity<ApiResponse<?>> getDetailAdminInquiry(@PathVariable String inquiryNo) {
        return ResponseEntity.ok(ApiResponse.success(inquiryQueryService.getDetailAdminInquiry(inquiryNo)));
    }

}
