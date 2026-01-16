package com.teamgold.goldenharvest.domain.customersupport.query.dto.response;

import lombok.Getter;

@Getter
public class InquiryDetailResponse {
    String title; //제목
    String body; //문의 내용
    String comment; //답변
    //todo 파일 추가
}
