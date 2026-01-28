package com.teamgold.goldenharvest.inquiry;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.common.infra.file.service.FileUploadService;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.comment.CommentCreateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.inquiry.InquiryCreateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.inquiry.InquiryUpdateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.service.impl.InquiryServiceImpl;
import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.File;
import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.Inquiry;
import com.teamgold.goldenharvest.domain.customersupport.command.infrastructure.repository.inquiry.InquiryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class InquiryServiceTest {
    @InjectMocks
    InquiryServiceImpl inquiryService;

    @Mock
    private InquiryRepository inquiryRepository;

    @Mock
    private FileUploadService fileUploadService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    @DisplayName("파일 없이 문의가 생성되고 이벤트가 발행된다.")
    void create_without_file() throws IOException {
        //given
        InquiryCreateRequest request = new InquiryCreateRequest(
                "INQUIRY001", "제목", " 내용"
        );

        //when
        inquiryService.create("rrrr@naver.com", request, null);

        //then
        verify(inquiryRepository, times(1)).save(any(Inquiry.class));

        verify(fileUploadService, never()).upload(any());
    }

    @Test
    @DisplayName("파일 포함 문의가 생성되고 이벤트가 발행된다.")
    void create() throws IOException {
        //given
        MultipartFile file = mock(MultipartFile.class);
        given(file.isEmpty()).willReturn(false);
        var uploadFile = mock(File.class);
        given(uploadFile.getFileId()).willReturn(10L);
        given(fileUploadService.upload(file)).willReturn(uploadFile);

        InquiryCreateRequest request = new InquiryCreateRequest(
                "INQUIRY001", "제목", " 내용"
        );

        //when
        inquiryService.create("rrrr@naver.com", request, file);

        //then
        verify(fileUploadService).upload(file);
        verify(inquiryRepository).save(any(Inquiry.class));
    }

    @Test
    @DisplayName("사용자 본인의 문의면 삭제된다.")
    void delete_inquiry() {
        //given
        Inquiry inquiry = mock(Inquiry.class);
        given(inquiryRepository.findByInquiryIdAndUserId("INQUIRY001", "rrrr@naver.com"))
                .willReturn(Optional.of(inquiry));
        //when
        inquiryService.delete("rrrr@naver.com", "INQUIRY001");
        //then
        verify(inquiryRepository).delete(inquiry);
    }

    @Test
    @DisplayName("문의가 없으면 삭제시 예외가 발생한다")
    void delete_inquiry_fail() {
        // given
        given(inquiryRepository.findByInquiryIdAndUserId("INQUIRY001", "rrrr@naver.com"))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                inquiryService.delete("rrrr@naver.com", "INQUIRY001")
        ).isInstanceOf(BusinessException.class)
                .satisfies(ex ->
                        assertThat(((BusinessException) ex).getErrorCode())
                                .isEqualTo(ErrorCode.INQUIRY_NOT_FOUND)
                );
    }

    @Test
    @DisplayName("문의 제목과 내용을 수정한다")
    void update_inquiry_without_file() throws IOException {
        // given
        Inquiry inquiry = mock(Inquiry.class);

        InquiryUpdateRequest request = new InquiryUpdateRequest(
                "제목", "내용"
        );

        given(inquiryRepository.findByInquiryIdAndUserId("INQUIRY001", "rrrr@naver.com"))
                .willReturn(Optional.of(inquiry));

        // when
        inquiryService.update("rrrr@naver.com", "INQUIRY001", request, null);

        // then
        verify(inquiry).updatedInquiry("제목", "내용");
        verify(fileUploadService, never()).upload(any());
    }
    @Test
    @DisplayName("파일이 있으면 등록 후 파일을 교체한다")
    void update_inquiry_with_file() throws IOException {
        // given
        Inquiry inquiry = mock(Inquiry.class);
        MultipartFile file = mock(MultipartFile.class);
        given(file.isEmpty()).willReturn(false);

        var uploadedFile = mock(File.class);
        given(uploadedFile.getFileId()).willReturn(20L);
        given(fileUploadService.upload(file)).willReturn(uploadedFile);

        InquiryUpdateRequest request = new InquiryUpdateRequest(
                "제목", "내용"
        );
        given(inquiryRepository.findByInquiryIdAndUserId("INQUIRY001", "rrrr@naver.com"))
                .willReturn(Optional.of(inquiry));
        // when
        inquiryService.update("rrrr@naver.com", "INQUIRY001", request, file);

        // then
        verify(fileUploadService).upload(file);
        verify(inquiry).updateFile(20L);
    }

    @Test
    @DisplayName("답변을 생성하면 문의 답변과 상태가 변경된다.")
    void comment() {
        //given
        Inquiry inquiry = mock(Inquiry.class);
        CommentCreateRequest request = new CommentCreateRequest("내용");
        given(inquiryRepository.findById("INQUIRY001"))
                .willReturn(Optional.of(inquiry));

        //when
        inquiryService.comment("INQUIRY001",request);

        //then
        verify(inquiry).updatedComment("내용");
        verify(inquiry).updatedProcessingStatus();

    }
    @Test
    @DisplayName("문의가 없으면 답변시 예외가 발생한다")
    void comment_fail_when_not_found() {
        // given
        given(inquiryRepository.findById("INQUIRY001"))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                inquiryService.comment(
                        "INQUIRY001",
                        mock(CommentCreateRequest.class)
                )
        )
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> {
                    BusinessException be = (BusinessException) ex;
                    assertThat(be.getErrorCode())
                            .isEqualTo(ErrorCode.INQUIRY_NOT_FOUND);
                });
    }


}
