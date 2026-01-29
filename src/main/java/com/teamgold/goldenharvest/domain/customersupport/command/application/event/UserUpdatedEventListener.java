package com.teamgold.goldenharvest.domain.customersupport.command.application.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.teamgold.goldenharvest.domain.customersupport.command.domain.snapshot.InquiryWriterSnapshot;
import com.teamgold.goldenharvest.domain.customersupport.command.infrastructure.repository.snapshot.InquiryWriterSnapshotRepository;
import com.teamgold.goldenharvest.domain.user.command.application.event.dto.UserUpdatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserUpdatedEventListener {

	private final InquiryWriterSnapshotRepository inquiryWriterSnapshotRepository;

	@EventListener
	public void updateInquiryWriterSnapshot(UserUpdatedEvent event) {
		InquiryWriterSnapshot snapshot = InquiryWriterSnapshot.builder()
			.name(event.name())
			.company(event.company())
			.phoneNumber(event.phoneNumber())
			.userEmail(event.email())
			.build();

		inquiryWriterSnapshotRepository.save(snapshot);
	}
}
