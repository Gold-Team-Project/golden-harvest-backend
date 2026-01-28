package com.teamgold.goldenharvest.domain.user.command.application.event;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SpringEventPublisher {

	private final ApplicationEventPublisher publisher;

	public void publishUpdatedUserDetails(UserUpdatedEvent event) {
		publisher.publishEvent(event);
	}

	public void publishAllUserDetails(List<UserUpdatedEvent> events) {
		events.forEach(publisher::publishEvent);
	}

}
