package com.teamgold.goldenharvest.domain.customersupport.query.dto.response;

import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.ProcessingStatus;
import lombok.Getter;

@Getter
public class AdminInquiryDetailResponse {
    String InquiryNo; //문의번호
    String createdAt; //문의일
    String company; //회사명
    String name; // 담당자
    String phoneNumber; //전화번호
    String email; //이메일
    String title; //제목
    String body; //본문
    String comment; //답변
    ProcessingStatus processingStatus; //처리 상태
    //파일
    Long fileId;
    String fileName;
    String contentType;
    String downloadUrl;

    public void updateUrl(String downloadUrl){
        this.downloadUrl = downloadUrl;
    }
}
