package com.teamgold.goldenharvest.inquiry;

import com.teamgold.goldenharvest.domain.customersupport.query.dto.response.AdminInquiryDetailResponse;
import com.teamgold.goldenharvest.domain.customersupport.query.dto.response.AdminInquiryListResponse;
import com.teamgold.goldenharvest.domain.customersupport.query.dto.response.InquiryDetailResponse;
import com.teamgold.goldenharvest.domain.customersupport.query.dto.response.InquiryListResponse;
import com.teamgold.goldenharvest.domain.customersupport.query.mapper.InquiryMapper;
import com.teamgold.goldenharvest.domain.customersupport.query.service.impl.InquiryQueryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class InquiryQueryServiceTest {
    @InjectMocks
    InquiryQueryServiceImpl inquiryQueryService;

    @Mock
    private InquiryMapper inquiryMapper;

    @Test
    @DisplayName("사용자 문의 목록을 페이징하여 조회한다")
    void getAllInquiry_success() {
        // given
        String userId = "user1";
        int page = 2;
        int size = 10;

        int expectedLimit = 10;
        int expectedOffset = 10;
        List<InquiryListResponse> list =
                List.of(mock(InquiryListResponse.class));
        given(inquiryMapper.findAllInquiry(userId, expectedLimit, expectedOffset))
                .willReturn(list);
        // when
        List<InquiryListResponse> responses = inquiryQueryService.getAllInquiry(userId, page, size);

        // then
        assertThat(responses).hasSize(1);
        verify(inquiryMapper).findAllInquiry(userId, expectedLimit, expectedOffset);
    }

    @Test
    @DisplayName("문의에 파일이 있으면 다운로드 URL을 생성한다")
    void getDetailInquiry_with_file() {
        // given
        InquiryDetailResponse dto = InquiryDetailResponse.builder()
                .fileId(100L)
                .build();
        given(inquiryMapper.findDetailInquiry("INQ001"))
                .willReturn(dto);
        // when
        InquiryDetailResponse result =
                inquiryQueryService.getDetailInquiry("INQ001");
        // then
        assertThat(result.getDownloadUrl())
                .isEqualTo("/files/100");
    }
    @Test
    @DisplayName("문의에 파일이 없으면 URL을 생성하지 않는다")
    void getDetailInquiry_without_file() {
        // given
        InquiryDetailResponse dto = InquiryDetailResponse.builder()
                .fileId(null)
                .build();

        given(inquiryMapper.findDetailInquiry("INQ002"))
                .willReturn(dto);

        // when
        InquiryDetailResponse result =
                inquiryQueryService.getDetailInquiry("INQ002");

        // then
        assertThat(result.getDownloadUrl()).isNull();
    }
    @Test
    @DisplayName("관리자_문의_목록을_페이징하여_조회한다")
    void getAllAdminInquiry_success() {
        // given
        int page = 1;
        int size = 20;

        int limit = 20;
        int offset = 0;

        List<AdminInquiryListResponse> list =
                List.of(mock(AdminInquiryListResponse.class));

        given(inquiryMapper.findAllAdminInquiry(limit, offset))
                .willReturn(list);

        // when
        List<AdminInquiryListResponse> result =
                inquiryQueryService.getAllAdminInquiry(page, size);

        // then
        assertThat(result).hasSize(1);
        verify(inquiryMapper).findAllAdminInquiry(limit, offset);
    }
    @Test
    @DisplayName("관리자 문의 상세에 파일이 있으면 URL을 생성한다")
    void getDetailAdminInquiry_with_file() {
        // given
        AdminInquiryDetailResponse dto = AdminInquiryDetailResponse.builder()
                .fileId(200L)
                .build();

        given(inquiryMapper.findDetailAdminInquiry("INQ100"))
                .willReturn(dto);

        // when
        AdminInquiryDetailResponse result =
                inquiryQueryService.getDetailAdminInquiry("INQ100");

        // then
        assertThat(result.getDownloadUrl())
                .isEqualTo("/files/200");
    }
    @Test
    @DisplayName("관리자 문의 상세에 파일이 없으면 URL을 생성하지 않는다")
    void getDetailAdminInquiry_without_file() {
        // given
        AdminInquiryDetailResponse dto = AdminInquiryDetailResponse.builder()
                .fileId(null)
                .build();

        given(inquiryMapper.findDetailAdminInquiry("INQ101"))
                .willReturn(dto);

        // when
        AdminInquiryDetailResponse result =
                inquiryQueryService.getDetailAdminInquiry("INQ101");

        // then
        assertThat(result.getDownloadUrl()).isNull();
    }





}
