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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InquiryQueryServiceTest {

    @InjectMocks
    InquiryQueryServiceImpl inquiryQueryService;

    @Mock
    private InquiryMapper inquiryMapper;

    @Test
    @DisplayName("사용자 문의 목록을 페이징하여 조회한다 (limit=size, offset=(page-1)*size)")
    void getAllInquiry_success() {
        // given
        String userId = "user1";
        int page = 2;
        int size = 10;

        int expectedLimit = 10;
        int expectedOffset = 10;

        List<InquiryListResponse> list = List.of(mock(InquiryListResponse.class));
        given(inquiryMapper.findAllInquiry(userId, expectedLimit, expectedOffset))
                .willReturn(list);

        // when
        List<InquiryListResponse> responses = inquiryQueryService.getAllInquiry(userId, page, size);

        // then
        assertThat(responses).hasSize(1);
        verify(inquiryMapper).findAllInquiry(userId, expectedLimit, expectedOffset);
        verifyNoMoreInteractions(inquiryMapper);
    }

    @Test
    @DisplayName("사용자 문의 상세를 조회한다 (매퍼 결과를 그대로 반환)")
    void getDetailInquiry_success() {
        // given
        InquiryDetailResponse dto = mock(InquiryDetailResponse.class);
        given(inquiryMapper.findDetailInquiry("INQ001")).willReturn(dto);

        // when
        InquiryDetailResponse result = inquiryQueryService.getDetailInquiry("INQ001");

        // then
        assertThat(result).isSameAs(dto);
        verify(inquiryMapper).findDetailInquiry("INQ001");
        verifyNoMoreInteractions(inquiryMapper);
    }

    @Test
    @DisplayName("관리자 문의 목록을 페이징하여 조회한다 (limit=size, offset=(page-1)*size)")
    void getAllAdminInquiry_success() {
        // given
        int page = 1;
        int size = 20;

        int expectedLimit = 20;
        int expectedOffset = 0;

        List<AdminInquiryListResponse> list = List.of(mock(AdminInquiryListResponse.class));
        given(inquiryMapper.findAllAdminInquiry(expectedLimit, expectedOffset))
                .willReturn(list);

        // when
        List<AdminInquiryListResponse> result = inquiryQueryService.getAllAdminInquiry(page, size);

        // then
        assertThat(result).hasSize(1);
        verify(inquiryMapper).findAllAdminInquiry(expectedLimit, expectedOffset);
        verifyNoMoreInteractions(inquiryMapper);
    }

    @Test
    @DisplayName("관리자 문의 상세를 조회한다 (매퍼 결과를 그대로 반환)")
    void getDetailAdminInquiry_success() {
        // given
        AdminInquiryDetailResponse dto = mock(AdminInquiryDetailResponse.class);
        given(inquiryMapper.findDetailAdminInquiry("INQ100")).willReturn(dto);

        // when
        AdminInquiryDetailResponse result = inquiryQueryService.getDetailAdminInquiry("INQ100");

        // then
        assertThat(result).isSameAs(dto);
        verify(inquiryMapper).findDetailAdminInquiry("INQ100");
        verifyNoMoreInteractions(inquiryMapper);
    }
}
