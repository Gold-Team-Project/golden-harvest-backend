package com.teamgold.goldenharvest.domain.customersupport.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
public class InquiryDetailResponse {
    String title; //제목
    String body; //문의 내용
    String comment; //답변
    //파일
    Long fileId;
    String fileName;
    String contentType;
    String downloadUrl;

    public void updateUrl(String downloadUrl){
        this.downloadUrl = downloadUrl;
    }
}
