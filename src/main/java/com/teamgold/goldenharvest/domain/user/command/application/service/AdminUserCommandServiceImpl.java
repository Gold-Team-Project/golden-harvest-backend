package com.teamgold.goldenharvest.domain.user.command.application.service;

import java.util.ArrayList;
import java.util.List;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.user.command.application.event.SpringEventPublisher;
import com.teamgold.goldenharvest.domain.user.command.application.event.UserUpdatedEvent;
import com.teamgold.goldenharvest.domain.user.command.domain.RequestStatus;
import com.teamgold.goldenharvest.domain.user.command.domain.User;
import com.teamgold.goldenharvest.domain.user.command.domain.UserStatus;
import com.teamgold.goldenharvest.domain.user.command.domain.UserUpdateApproval;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserUpdateApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserCommandServiceImpl implements AdminUserCommandService {

    private final UserRepository userRepository;
    private final UserUpdateApprovalRepository userUpdateApprovalRepository;
	private final SpringEventPublisher springEventPublisher;

    @Override
    public void approveUser(String email, UserStatus newStatus) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getStatus() != UserStatus.PENDING) {
           throw  new BusinessException(ErrorCode.USER_ONLY_PENDING_CAN_BE_APPROVED);
        }

        user.updateStatus(newStatus);
    }

    @Override
    @Transactional
    public void approveProfileUpdate(Long approvalId) {
        // 1. "승인 대기 건(엔티티)"을 찾습니다.
        UserUpdateApproval approval = userUpdateApprovalRepository.findById(approvalId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));

        if (approval.getStatus() != RequestStatus.PENDING) {
            throw new BusinessException(ErrorCode.DUPLICATE_REQUEST);
        }

        // 2. 승인 처리 (상태 변경)
        approval.approve();

        // 3. 실제 유저 엔티티 정보 업데이트
        User user = approval.getUser();

        user.updateBusinessInfo(
                approval.getRequestCompany(),
                approval.getRequestBusinessNumber(),
                approval.getRequestFileId()
        );
    }

	@Override
	public void publishAllUserDetailsEvent() {
		List<User> users = userRepository.findAll();

		List<UserUpdatedEvent> userUpdatedEvents = users.stream().map(
			user -> UserUpdatedEvent.builder()
				.email(user.getEmail())
				.name(user.getName())
				.businessNumber(String.valueOf(user.getBusinessNumber()))
				.addressLine1(user.getAddressLine1())
				.postalCode(user.getPostalCode())
				.phoneNumber(String.valueOf(user.getPhoneNumber()))
				.company(user.getCompany())
				.addressLine2(user.getAddressLine2())
				.build()
		).toList();

		springEventPublisher.publishAllUserDetails(userUpdatedEvents);
	}

    @Override
    @Transactional
    public void updateUserStatus(String targetEmail, UserStatus newStatus, String adminEmail) {
        // 본인 계정인지 확인
    if (targetEmail.equals(adminEmail)) {
        throw new BusinessException(ErrorCode.INVALID_REQUEST);
    }

    User user = userRepository.findByEmail(targetEmail)
            .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

    user.updateStatus(newStatus);
    }
}

