package com.teamgold.goldenharvest.domain.sales.command.application;


import com.teamgold.goldenharvest.domain.sales.command.application.service.SalesOrderCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventConsumer {
    private final SalesOrderCommandService salesOrderCommandService; // 이메일 업데이트 로직을 담을 서비스
    // User 서비스에서 발행한 'user-topic'을 구독
    @KafkaListener(topics = "user-topic", groupId = "sales-group")
    public void handleUserUpdate(UserUpdatedEvent event) { // UserUpdatedEvent는 DTO 클래스
        log.info("Received user update event for user ID: {}", event.getUserId());
        // SalesOrder 테이블의 userEmail을 업데이트하는 로직 호출
        salesOrderCommandService.updateUserEmail(event.getUserId(), event.getNewEmail());
    }
}
