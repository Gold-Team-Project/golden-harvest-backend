package com.teamgold.goldenharvest.domain.sales.command.application.event.listener;

import com.teamgold.goldenharvest.domain.sales.command.domain.customer.Customer;
import com.teamgold.goldenharvest.domain.sales.command.infrastructure.repository.CustomerRepository;
import com.teamgold.goldenharvest.domain.user.command.application.event.UserUpdatedEvent;
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
                .email(event.getEmail())
                .company(event.getCompany())
                .businessNumber(event.getBusinessNumber())
                .name(event.getName())
                .phoneNumber(event.getPhoneNumber())
                .addressLine1(event.getAddressLine1())
                .addressLine2(event.getAddressLine2())
                .postalCode(event.getPostalCode())
                .build();
        
        customerRepository.save(customer);
    }
}