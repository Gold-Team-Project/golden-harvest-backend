package com.teamgold.goldenharvest.domain.customersupport.command.application.event.listener;

import com.teamgold.goldenharvest.domain.customersupport.command.application.event.InquiryWriterResolvedEvent;
import com.teamgold.goldenharvest.domain.customersupport.command.domain.snapshot.InquiryWriterSnapshot;
import com.teamgold.goldenharvest.domain.customersupport.command.infrastructure.repository.snapshot.InquiryWriterSnapshotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InquiryWriterSnapshotEventListener {

    private final InquiryWriterSnapshotRepository snapshotRepository;

    @EventListener
    @Transactional
    public void handle(InquiryWriterResolvedEvent event) {

        InquiryWriterSnapshot snapshot =
                new InquiryWriterSnapshot(
                        event.inquiryId(),
                        event.userId(),
                        event.company(),
                        event.name(),
                        event.phoneNumber(),
                        event.email()
                );

        snapshotRepository.save(snapshot);
    }
}
