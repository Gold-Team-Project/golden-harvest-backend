package com.teamgold.goldenharvest.domain.customersupport.query.service.impl;

import com.teamgold.goldenharvest.domain.customersupport.query.dto.response.AdminInquiryDetailResponse;
import com.teamgold.goldenharvest.domain.customersupport.query.dto.response.AdminInquiryListResponse;
import com.teamgold.goldenharvest.domain.customersupport.query.dto.response.InquiryDetailResponse;
import com.teamgold.goldenharvest.domain.customersupport.query.dto.response.InquiryListResponse;
import com.teamgold.goldenharvest.domain.customersupport.query.mapper.InquiryMapper;
import com.teamgold.goldenharvest.domain.customersupport.query.service.InquiryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryQueryServiceImpl implements InquiryQueryService {
    private final InquiryMapper inquiryMapper;

    @Override
    public List<InquiryListResponse> getAllInquiry(String userId, Integer page, Integer size) {
        int limit = size;
        int offset = (page - 1) * limit;
        return inquiryMapper.findAllInquiry(userId, limit, offset);
    }

    @Override
    public InquiryDetailResponse getDetailInquiry(String inquiryNo) {
        return inquiryMapper.findDetailInquiry(inquiryNo); // 매퍼에서 이미 URL과 파일명을 담아옴
    }

    @Override
    public List<AdminInquiryListResponse> getAllAdminInquiry(Integer page, Integer size) {
        int limit = size;
        int offset = (page - 1) * limit;
        return inquiryMapper.findAllAdminInquiry(limit, offset);
    }
    @Override
    public AdminInquiryDetailResponse getDetailAdminInquiry(String inquiryNo) {
        return inquiryMapper.findDetailAdminInquiry(inquiryNo);
    }
}
