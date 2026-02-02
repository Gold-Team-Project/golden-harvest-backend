package com.teamgold.goldenharvest.purchases;

import com.teamgold.goldenharvest.domain.purchases.command.application.event.PurchaseOrderCreatedEvent;
import com.teamgold.goldenharvest.domain.purchases.command.application.service.PurchaseCommandService;
import com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate.OrderStatus;
import com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate.PurchaseOrder;
import com.teamgold.goldenharvest.domain.purchases.command.domain.repository.PurchaseOrderRepository;
import com.teamgold.goldenharvest.domain.purchases.command.infrastructure.repository.JpaOrderStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class PurchasesCommandServiceTest {

    @InjectMocks
    private PurchaseCommandService purchaseCommandService;

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private JpaOrderStatusRepository orderStatusRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    void createPurchaseOrder_createsPurchaseOrderSuccessfully() {
        // given
        Long quantity = 100L;
        String skuNo = "SKU-12345";

        OrderStatus draftStatus = OrderStatus.builder()
                .type("DRAFT")
                .build();

        // FIX: 서비스가 findById를 쓰면 여기도 findById로 스텁
        given(orderStatusRepository.findById("DRAFT"))
                .willReturn(Optional.of(draftStatus));

        // (선택) 서비스가 CREATED도 조회하면 이것도 필요
        // OrderStatus createdStatus = OrderStatus.builder().type("CREATED").build();
        // given(orderStatusRepository.findById("CREATED")).willReturn(Optional.of(createdStatus));

        // when
        String poId = purchaseCommandService.createPurchaseOrder(quantity, skuNo);

        // then
        ArgumentCaptor<PurchaseOrder> poCaptor = ArgumentCaptor.forClass(PurchaseOrder.class);
        then(purchaseOrderRepository).should(times(1)).save(poCaptor.capture());

        PurchaseOrder savedPo = poCaptor.getValue();
        assertThat(savedPo.getPurchase_order_id()).startsWith("PO");
        assertThat(savedPo.getPurchase_order_id()).hasSize(18);
        assertThat(savedPo.getSkuNo()).isEqualTo(skuNo);
        assertThat(savedPo.getQuantity()).isEqualTo(100);

        // 주의: 서비스에서 orderStatus를 DRAFT로 넣는지 CREATED로 덮는지에 따라 기대값이 달라짐
        // 서비스가 DRAFT만 set한다면:
        assertThat(savedPo.getOrderStatus()).isEqualTo(draftStatus);
        // 서비스가 CREATED로 마지막에 덮어쓴다면:
        // assertThat(savedPo.getOrderStatus()).isEqualTo(createdStatus);

        assertThat(savedPo.getCreatedAt()).isNotNull();
        assertThat(savedPo.getDeliveryDate()).isNull();

        assertThat(poId).startsWith("PO");
        assertThat(poId).hasSize(18);
    }

    @Test
    void createPurchaseOrder_publishesEvent() {
        // given
        Long quantity = 50L;
        String skuNo = "SKU-67890";

        OrderStatus draftStatus = OrderStatus.builder()
                .type("DRAFT")
                .build();

        // FIX
        given(orderStatusRepository.findById("DRAFT"))
                .willReturn(Optional.of(draftStatus));

        // (선택) 서비스가 CREATED도 조회하면 이것도 필요
        // OrderStatus createdStatus = OrderStatus.builder().type("CREATED").build();
        // given(orderStatusRepository.findById("CREATED")).willReturn(Optional.of(createdStatus));

        // when
        purchaseCommandService.createPurchaseOrder(quantity, skuNo);

        // then
        ArgumentCaptor<PurchaseOrderCreatedEvent> eventCaptor = ArgumentCaptor.forClass(PurchaseOrderCreatedEvent.class);
        then(applicationEventPublisher).should(times(1)).publishEvent(eventCaptor.capture());

        PurchaseOrderCreatedEvent event = eventCaptor.getValue();
        assertThat(event.purchaseOrderId()).startsWith("PO");
        assertThat(event.skuNo()).isEqualTo(skuNo);
        assertThat(event.quantity()).isEqualTo(50);
        assertThat(event.createdAt()).isNotNull();
    }

    @Test
    void createPurchaseOrder_throwsException_whenQuantityIsNull() {
        // given
        Long quantity = null;
        String skuNo = "SKU-12345";

        // when & then
        assertThatThrownBy(() -> purchaseCommandService.createPurchaseOrder(quantity, skuNo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 0 보다 커야 합니다");

        then(purchaseOrderRepository).should(never()).save(any());
        then(applicationEventPublisher).should(never()).publishEvent(any());
    }

    @Test
    void createPurchaseOrder_throwsException_whenQuantityIsZero() {
        Long quantity = 0L;
        String skuNo = "SKU-12345";

        assertThatThrownBy(() -> purchaseCommandService.createPurchaseOrder(quantity, skuNo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 0 보다 커야 합니다");

        then(purchaseOrderRepository).should(never()).save(any());
        then(applicationEventPublisher).should(never()).publishEvent(any());
    }

    @Test
    void createPurchaseOrder_throwsException_whenQuantityIsNegative() {
        Long quantity = -10L;
        String skuNo = "SKU-12345";

        assertThatThrownBy(() -> purchaseCommandService.createPurchaseOrder(quantity, skuNo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 0 보다 커야 합니다");

        then(purchaseOrderRepository).should(never()).save(any());
        then(applicationEventPublisher).should(never()).publishEvent(any());
    }

    @Test
    void createPurchaseOrder_throwsException_whenSkuNoIsNull() {
        Long quantity = 100L;
        String skuNo = null;

        assertThatThrownBy(() -> purchaseCommandService.createPurchaseOrder(quantity, skuNo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품명을 찾을 수 없습니다");

        then(purchaseOrderRepository).should(never()).save(any());
        then(applicationEventPublisher).should(never()).publishEvent(any());
    }

    @Test
    void createPurchaseOrder_throwsException_whenSkuNoIsBlank() {
        Long quantity = 100L;
        String skuNo = "   ";

        assertThatThrownBy(() -> purchaseCommandService.createPurchaseOrder(quantity, skuNo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품명을 찾을 수 없습니다");

        then(purchaseOrderRepository).should(never()).save(any());
        then(applicationEventPublisher).should(never()).publishEvent(any());
    }

    @Test
    void createPurchaseOrder_throwsException_whenQuantityExceedsIntMax() {
        Long quantity = (long) Integer.MAX_VALUE + 1L;
        String skuNo = "SKU-12345";

        assertThatThrownBy(() -> purchaseCommandService.createPurchaseOrder(quantity, skuNo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량이 너무 큽니다");

        then(purchaseOrderRepository).should(never()).save(any());
        then(applicationEventPublisher).should(never()).publishEvent(any());
    }

    @Test
    void createPurchaseOrder_handlesMaxValidQuantity() {
        // given
        Long quantity = (long) Integer.MAX_VALUE;
        String skuNo = "SKU-12345";

        OrderStatus draftStatus = OrderStatus.builder()
                .type("DRAFT")
                .build();

        // FIX: 이게 정답
        given(orderStatusRepository.findById("DRAFT"))
                .willReturn(Optional.of(draftStatus));

        // (선택) 서비스가 CREATED도 조회하면 이것도 필요
        // OrderStatus createdStatus = OrderStatus.builder().type("CREATED").build();
        // given(orderStatusRepository.findById("CREATED")).willReturn(Optional.of(createdStatus));

        // when
        String poId = purchaseCommandService.createPurchaseOrder(quantity, skuNo);

        // then
        ArgumentCaptor<PurchaseOrder> poCaptor = ArgumentCaptor.forClass(PurchaseOrder.class);
        then(purchaseOrderRepository).should(times(1)).save(poCaptor.capture());

        PurchaseOrder savedPo = poCaptor.getValue();
        assertThat(savedPo.getQuantity()).isEqualTo(Integer.MAX_VALUE);
        assertThat(poId).isNotNull();
    }
}
