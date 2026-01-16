package com.teamgold.goldenharvest.domain.customersupport.command.application.event.listener;

import com.teamgold.goldenharvest.domain.customersupport.command.application.event.InquiryCreatedEvent;
import com.teamgold.goldenharvest.domain.customersupport.command.application.event.InquiryWriterResolvedEvent;
import com.teamgold.goldenharvest.domain.user.command.domain.User;
import com.teamgold.goldenharvest.domain.user.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
//user 이벤트리스너
public class InquiryCreatedEventListener {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    public void handle(InquiryCreatedEvent event) {

        User user = userRepository.findById(event.userId())
                .orElseThrow();

        eventPublisher.publishEvent(
                new InquiryWriterResolvedEvent(
                        event.inquiryId(),
                        user.getEmail(),
                        user.getCompany(),
                        user.getName(),
                        user.getPhoneNumber(),
                        user.getEmail()
                )
        );
    }
}
