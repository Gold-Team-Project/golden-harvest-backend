package com.teamgold.goldenharvest.domain.customersupport.command.application.event.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.teamgold.goldenharvest.domain.customersupport.command.domain.snapshot.InquiryWriterSnapshot;
import com.teamgold.goldenharvest.domain.customersupport.command.infrastructure.repository.snapshot.InquiryWriterSnapshotRepository;
import com.teamgold.goldenharvest.domain.user.command.application.event.UserUpdatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDetailUpdateEventListener {

	private final InquiryWriterSnapshotRepository inquiryWriterSnapshotRepository;

	@EventListener
	public void updateInquiryWriterSnapshot(UserUpdatedEvent event) {
		InquiryWriterSnapshot snapshot = InquiryWriterSnapshot.builder()
			.name(event.getName())
			.company(event.getCompany())
			.phoneNumber(event.getPhoneNumber())
			.userEmail(event.getEmail())
			.build();

		inquiryWriterSnapshotRepository.save(snapshot);
	}
}
