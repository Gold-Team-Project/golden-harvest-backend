package com.teamgold.goldenharvest.domain.customersupport.command.application.service;

import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.comment.CommentCreateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.inquiry.InquiryCreateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.inquiry.InquiryUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public interface InquiryService {

    //문의 등록
    void create(String userId, String salesOrderId, InquiryCreateRequest request);
    //문의 삭제
    void delete(String inquiryNo);
    //문의 수정
    void update(String inquiryNo, InquiryUpdateRequest request);
    //문의 답변
    void comment (String inquiryNo, CommentCreateRequest request);

}
