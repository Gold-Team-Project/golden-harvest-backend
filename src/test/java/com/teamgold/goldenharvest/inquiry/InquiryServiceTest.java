package com.teamgold.goldenharvest.inquiry;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.common.infra.file.domain.File;
import com.teamgold.goldenharvest.common.infra.file.service.FileUploadService;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.comment.CommentCreateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.inquiry.InquiryCreateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.inquiry.InquiryUpdateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.service.impl.InquiryServiceImpl;
import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.Inquiry;
import com.teamgold.goldenharvest.domain.customersupport.command.infrastructure.repository.inquiry.InquiryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InquiryServiceTest {

    @InjectMocks
    InquiryServiceImpl inquiryService;

    @Mock
    private InquiryRepository inquiryRepository;

    @Mock
    private FileUploadService fileUploadService;

    @Test
    @DisplayName("파일 없이 문의가 생성된다.")
    void create_without_file() throws IOException {
        // given
        InquiryCreateRequest request = new InquiryCreateRequest(
                "INQUIRY001", "제목", "내용"
        );

        // when
        inquiryService.create("rrrr@naver.com", request, null);

        // then
        verify(inquiryRepository, times(1)).save(any(Inquiry.class));
        verify(fileUploadService, never()).upload(any());
    }

    @Test
    @DisplayName("파일 포함 문의가 생성된다. (업로드한 fileUrl 저장)")
    void create_with_file() throws IOException {
        // given
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);

        File uploadedFile = File.builder()
                .fileUrl("https://s3.aws.com/inquiry.png")
                .build();
        when(fileUploadService.upload(file)).thenReturn(uploadedFile);

        InquiryCreateRequest request = new InquiryCreateRequest(
                "INQUIRY001", "제목", "내용"
        );

        // when
        inquiryService.create("rrrr@naver.com", request, file);

        // then
        verify(fileUploadService, times(1)).upload(file);

        ArgumentCaptor<Inquiry> captor = ArgumentCaptor.forClass(Inquiry.class);
        verify(inquiryRepository, times(1)).save(captor.capture());

        Inquiry saved = captor.getValue();
        assertThat(saved.getUserId()).isEqualTo("rrrr@naver.com");
        assertThat(saved.getSalesOrderId()).isEqualTo("INQUIRY001");
        assertThat(saved.getTitle()).isEqualTo("제목");
        assertThat(saved.getBody()).isEqualTo("내용");
        assertThat(saved.getFileUrl()).isEqualTo("https://s3.aws.com/inquiry.png");
    }

    @Test
    @DisplayName("사용자 본인의 문의면 삭제된다.")
    void delete_inquiry() {
        // given
        Inquiry inquiry = mock(Inquiry.class);
        when(inquiryRepository.findByInquiryIdAndUserId("INQUIRY001", "rrrr@naver.com"))
                .thenReturn(Optional.of(inquiry));

        // when
        inquiryService.delete("rrrr@naver.com", "INQUIRY001");

        // then
        verify(inquiryRepository, times(1)).delete(inquiry);
    }

    @Test
    @DisplayName("문의가 없으면 삭제 시 예외가 발생한다.")
    void delete_inquiry_fail() {
        // given
        when(inquiryRepository.findByInquiryIdAndUserId("INQUIRY001", "rrrr@naver.com"))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> inquiryService.delete("rrrr@naver.com", "INQUIRY001"))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INQUIRY_NOT_FOUND);
    }

    @Test
    @DisplayName("문의 제목과 내용을 수정한다 (파일 없음)")
    void update_inquiry_without_file() throws IOException {
        // given
        Inquiry inquiry = mock(Inquiry.class);
        InquiryUpdateRequest request = new InquiryUpdateRequest("제목", "내용");

        when(inquiryRepository.findByInquiryIdAndUserId("INQUIRY001", "rrrr@naver.com"))
                .thenReturn(Optional.of(inquiry));

        // when
        inquiryService.update("rrrr@naver.com", "INQUIRY001", request, null);

        // then
        verify(inquiry, times(1)).updatedInquiry("제목", "내용");
        verify(fileUploadService, never()).upload(any());
        verify(inquiry, never()).updateFile(anyString());
    }

    @Test
    @DisplayName("파일이 있으면 업로드 후 파일을 교체한다 (fileUrl로 updateFile)")
    void update_inquiry_with_file() throws IOException {
        // given
        Inquiry inquiry = mock(Inquiry.class);
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);

        File uploadedFile = File.builder()
                .fileUrl("https://s3.aws.com/inquiry_new.png")
                .build();
        when(fileUploadService.upload(file)).thenReturn(uploadedFile);

        InquiryUpdateRequest request = new InquiryUpdateRequest("제목", "내용");

        when(inquiryRepository.findByInquiryIdAndUserId("INQUIRY001", "rrrr@naver.com"))
                .thenReturn(Optional.of(inquiry));

        // when
        inquiryService.update("rrrr@naver.com", "INQUIRY001", request, file);

        // then
        verify(inquiry, times(1)).updatedInquiry("제목", "내용");
        verify(fileUploadService, times(1)).upload(file);
        verify(inquiry, times(1)).updateFile("https://s3.aws.com/inquiry_new.png");
    }

    @Test
    @DisplayName("문의가 없으면 수정 시 예외가 발생한다.")
    void update_inquiry_fail_when_not_found() {
        // given
        when(inquiryRepository.findByInquiryIdAndUserId("INQUIRY001", "rrrr@naver.com"))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                inquiryService.update("rrrr@naver.com", "INQUIRY001",
                        new InquiryUpdateRequest("제목", "내용"), null)
        )
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INQUIRY_NOT_FOUND);
    }

    @Test
    @DisplayName("답변을 생성하면 문의 답변과 상태가 변경된다.")
    void comment_success() {
        // given
        Inquiry inquiry = mock(Inquiry.class);
        CommentCreateRequest request = new CommentCreateRequest("내용");

        when(inquiryRepository.findById("INQUIRY001"))
                .thenReturn(Optional.of(inquiry));

        // when
        inquiryService.comment("INQUIRY001", request);

        // then
        verify(inquiry, times(1)).updatedComment("내용");
        verify(inquiry, times(1)).updatedProcessingStatus();
    }

    @Test
    @DisplayName("comment가 null이면 답변/상태 변경을 하지 않는다.")
    void comment_null_should_do_nothing() {
        // given
        Inquiry inquiry = mock(Inquiry.class);
        CommentCreateRequest request = new CommentCreateRequest(null);

        when(inquiryRepository.findById("INQUIRY001"))
                .thenReturn(Optional.of(inquiry));

        // when
        inquiryService.comment("INQUIRY001", request);

        // then
        verify(inquiry, never()).updatedComment(any());
        verify(inquiry, never()).updatedProcessingStatus();
    }

    @Test
    @DisplayName("문의가 없으면 답변 시 예외가 발생한다.")
    void comment_fail_when_not_found() {
        // given
        when(inquiryRepository.findById("INQUIRY001"))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> inquiryService.comment("INQUIRY001", new CommentCreateRequest("내용")))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INQUIRY_NOT_FOUND);
    }
}
