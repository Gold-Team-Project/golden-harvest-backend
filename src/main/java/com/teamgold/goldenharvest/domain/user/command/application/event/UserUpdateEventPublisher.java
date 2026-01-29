package com.teamgold.goldenharvest.domain.user.command.application.event;

import com.teamgold.goldenharvest.domain.user.command.application.event.dto.UserUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserUpdateEventPublisher {

	private final ApplicationEventPublisher publisher;

	public void publishUpdatedUserDetails(UserUpdatedEvent event) {
		publisher.publishEvent(event);
	}
}
