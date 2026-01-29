package com.teamgold.goldenharvest.domain.sales.command.application.event;

import com.teamgold.goldenharvest.domain.sales.command.domain.customer.Customer;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.repository.CustomerRepository;
import com.teamgold.goldenharvest.domain.user.command.application.event.dto.UserUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CustomerEventListener {

    private final CustomerRepository customerRepository;

    @EventListener
    @Transactional
    public void handleUserUpdatedEvent(UserUpdatedEvent event) {
        Customer customer = Customer.builder()
                .email(event.email())
                .company(event.company())
                .businessNumber(event.businessNumber())
                .name(event.name())
                .phoneNumber(event.phoneNumber())
                .addressLine1(event.addressLine1())
                .addressLine2(event.addressLine2())
                .postalCode(event.postalCode())
                .build();
        
        customerRepository.save(customer);
    }
}