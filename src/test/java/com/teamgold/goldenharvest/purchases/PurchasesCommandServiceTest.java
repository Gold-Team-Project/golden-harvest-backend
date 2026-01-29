package com.teamgold.goldenharvest.purchases;

import com.teamgold.goldenharvest.domain.purchases.command.application.event.PurchaseOrderCreatedEvent;
import com.teamgold.goldenharvest.domain.purchases.command.application.service.PurchaseCommandService;
import com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate.OrderStatus;
import com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate.PurchaseOrder;
import com.teamgold.goldenharvest.domain.purchases.command.domain.repository.OrderStatusRepository;
import com.teamgold.goldenharvest.domain.purchases.command.domain.repository.PurchaseOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

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
    private OrderStatusRepository orderStatusRepository;

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

        given(orderStatusRepository.findByType("DRAFT")).willReturn(draftStatus);

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
        assertThat(savedPo.getOrderStatus()).isEqualTo(draftStatus);
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

        given(orderStatusRepository.findByType("DRAFT")).willReturn(draftStatus);

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
        // given
        Long quantity = 0L;
        String skuNo = "SKU-12345";

        // when & then
        assertThatThrownBy(() -> purchaseCommandService.createPurchaseOrder(quantity, skuNo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 0 보다 커야 합니다");

        then(purchaseOrderRepository).should(never()).save(any());
        then(applicationEventPublisher).should(never()).publishEvent(any());
    }

    @Test
    void createPurchaseOrder_throwsException_whenQuantityIsNegative() {
        // given
        Long quantity = -10L;
        String skuNo = "SKU-12345";

        // when & then
        assertThatThrownBy(() -> purchaseCommandService.createPurchaseOrder(quantity, skuNo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 0 보다 커야 합니다");

        then(purchaseOrderRepository).should(never()).save(any());
        then(applicationEventPublisher).should(never()).publishEvent(any());
    }

    @Test
    void createPurchaseOrder_throwsException_whenSkuNoIsNull() {
        // given
        Long quantity = 100L;
        String skuNo = null;

        // when & then
        assertThatThrownBy(() -> purchaseCommandService.createPurchaseOrder(quantity, skuNo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품명을 찾을 수 없습니다");

        then(purchaseOrderRepository).should(never()).save(any());
        then(applicationEventPublisher).should(never()).publishEvent(any());
    }

    @Test
    void createPurchaseOrder_throwsException_whenSkuNoIsBlank() {
        // given
        Long quantity = 100L;
        String skuNo = "   ";

        // when & then
        assertThatThrownBy(() -> purchaseCommandService.createPurchaseOrder(quantity, skuNo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품명을 찾을 수 없습니다");

        then(purchaseOrderRepository).should(never()).save(any());
        then(applicationEventPublisher).should(never()).publishEvent(any());
    }

    @Test
    void createPurchaseOrder_throwsException_whenQuantityExceedsIntMax() {
        // given
        Long quantity = (long) Integer.MAX_VALUE + 1L;
        String skuNo = "SKU-12345";

        // when & then
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

        given(orderStatusRepository.findByType("DRAFT")).willReturn(draftStatus);

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
