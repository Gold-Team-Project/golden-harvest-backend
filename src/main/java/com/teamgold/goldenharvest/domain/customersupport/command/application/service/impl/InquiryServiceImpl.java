package com.teamgold.goldenharvest.domain.customersupport.command.application.service.impl;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.comment.CommentCreateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.inquiry.InquiryCreateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.inquiry.InquiryUpdateRequest;
import com.teamgold.goldenharvest.domain.customersupport.command.application.event.InquiryCreatedEvent;
import com.teamgold.goldenharvest.domain.customersupport.command.application.service.InquiryService;
import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.Inquiry;
import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.ProcessingStatus;
import com.teamgold.goldenharvest.domain.customersupport.command.infrastructure.repository.inquiry.InquiryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
    private final InquiryRepository inquiryRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void create(String userId,InquiryCreateRequest request) {
        // todo file 연동 구현
        String inquiryId = LocalDateTime.now() + "-" + "Inquiry" + "-" + UUID.randomUUID();
        Inquiry inquiry = Inquiry.builder()
                .inquiryId(inquiryId)
                .userId(userId)
                .salesOrderId(request.salesOrderId())
                .title(request.title())
                .body(request.body())
                .processingStatus(ProcessingStatus.N)
                .build();
        inquiryRepository.save(inquiry);

        eventPublisher.publishEvent(
                new InquiryCreatedEvent(inquiryId, userId)
        );
    }

    @Override
    @Transactional
    public void delete(String userId,String inquiryNo) {
        Inquiry inquiry = inquiryRepository.findByInquiryIdAndUserId(inquiryNo,userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INQUIRY_NOT_FOUND));

        inquiryRepository.delete(inquiry);


    }

    @Override
    @Transactional
    public void update(String userId,String inquiryNo, InquiryUpdateRequest request) {
        // todo file 연동 구현
        Inquiry inquiry = inquiryRepository.findByInquiryIdAndUserId(inquiryNo,userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INQUIRY_NOT_FOUND));

        inquiry.updatedInquiry(request.title(), request.body());
    }

    @Override
    @Transactional
    public void comment(String inquiryNo, CommentCreateRequest request) {
        Inquiry inquiry = inquiryRepository.findById(inquiryNo)
                .orElseThrow(() -> new BusinessException(ErrorCode.INQUIRY_NOT_FOUND));

        if (request.getComment() != null) {
            inquiry.updatedComment(request.getComment()); // 답변 작성
            inquiry.updatedProcessingStatus(); // 문의 상태 변경
        }


    }


}
