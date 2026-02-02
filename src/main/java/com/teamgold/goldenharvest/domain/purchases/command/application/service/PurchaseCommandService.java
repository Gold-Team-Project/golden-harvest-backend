package com.teamgold.goldenharvest.domain.purchases.command.application.service;

import com.teamgold.goldenharvest.domain.notification.command.domain.repository.NotificationRepository;
import com.teamgold.goldenharvest.domain.purchases.command.application.event.PurchaseOrderCreatedEvent;
import com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate.OrderStatus;
import com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate.PurchaseOrder;
import com.teamgold.goldenharvest.domain.purchases.command.domain.repository.OrderStatusRepository;
import com.teamgold.goldenharvest.domain.purchases.command.domain.repository.PurchaseOrderRepository;
import com.teamgold.goldenharvest.domain.purchases.command.infrastructure.repository.JpaOrderStatusRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseCommandService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final JpaOrderStatusRepository orderStatusRepository; // 유지
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public String createPurchaseOrder(Long quantity, String skuNo) {
        // 1) 입력값 검증
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("수량은 0 보다 커야 합니다");
        }
        if (skuNo == null || skuNo.isBlank()) {
            throw new IllegalArgumentException("상품명을 찾을 수 없습니다");
        }
        if (quantity > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("수량이 너무 큽니다");
        }

        // 2) 상태 조회 (초기 상태: DRAFT)
        OrderStatus draft = orderStatusRepository.findById("DRAFT")
                .orElseThrow(() -> new IllegalStateException("주문 상태 없음: DRAFT"));

        // 3) PO ID 생성
        String poId = generatePoId();

        // 4) 엔티티 생성 (orderStatus는 한 번만!)
        PurchaseOrder po = PurchaseOrder.builder()
                .purchase_order_id(poId)
                .orderStatus(draft)
                .createdAt(LocalDate.now())
                .deliveryDate(null)
                .skuNo(skuNo)
                .quantity(quantity.intValue())
                .build();

        // 5) 저장
        purchaseOrderRepository.save(po);

        // 6) 이벤트 발행
        applicationEventPublisher.publishEvent(
                new PurchaseOrderCreatedEvent(
                        poId,
                        LocalDate.now(),
                        skuNo,
                        quantity.intValue()
                )
        );

        return poId;
    }

    private String generatePoId() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String rand = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "PO" + date + rand;
    }
}

