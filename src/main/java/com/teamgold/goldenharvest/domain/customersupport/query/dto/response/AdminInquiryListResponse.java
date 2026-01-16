package com.teamgold.goldenharvest.domain.customersupport.query.dto.response;

import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.ProcessingStatus;
import lombok.Getter;

@Getter
public class AdminInquiryListResponse {
    String InquiryNo; //문의번호
    String createdAt; //등록일
    String company; //회사명
    String title; //제목
    ProcessingStatus processingStatus; //처리 상태

}
