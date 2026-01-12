package com.teamgold.goldenharvest.domain.purchases.command.application.service;

import com.teamgold.goldenharvest.domain.notification.command.domain.repository.NotificationRepository;
import com.teamgold.goldenharvest.domain.purchases.command.application.event.DomainEventPublisher;
import com.teamgold.goldenharvest.domain.purchases.command.application.event.PurchaseOrderCreatedEvent;
import com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate.OrderStatus;
import com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate.PurchaseOrder;
import com.teamgold.goldenharvest.domain.purchases.command.domain.repository.OrderStatusRepository;
import com.teamgold.goldenharvest.domain.purchases.command.domain.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseCommandService {

    private final PurchaseOrderRepository PurchaseOrderRepository;
    private final OrderStatusRepository OrderStatusRepository;
    private final DomainEventPublisher eventPublisher;
    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

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

        // 2) 기본 상태 조회 (예: DRAFT)
        // 너 OrderStatus 엔티티/컬럼명에 맞춰 findBy... 는 바꿔야 함
        OrderStatus draft = OrderStatusRepository.findByType("DRAFT");

        // 3) PO ID 생성 (20자 제한 고려)
        String poId = generatePoId(); // 예: PO20260112A1B2C3D4 (18자)

        // 4) 엔티티 생성 (Builder)
        PurchaseOrder po = PurchaseOrder.builder()
                .purchase_order_id(poId)
                .orderStatus(draft)
                .createdAt(LocalDate.now())
                .deliveryDate(null)               // 지금은 없으면 null
                .skuNo(skuNo)
                .quantity(quantity.intValue())
                .build();

        // 5) 저장
        PurchaseOrderRepository.save(po);
        eventPublisher.publish(
                new PurchaseOrderCreatedEvent(
                        poId
                        ,LocalDate.now()
                        ,quantity.intValue()
                )
        );


        return po.getPurchase_order_id();
    }

    private String generatePoId() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // yyyymmdd
        String rand = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "PO" + date + rand;
    }
}
