package com.teamgold.goldenharvest.inventory;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.domain.inventory.command.application.dto.PurchaseOrderEvent;
import com.teamgold.goldenharvest.domain.inventory.command.application.service.InboundService;
import com.teamgold.goldenharvest.domain.inventory.command.application.service.LotService;
import com.teamgold.goldenharvest.domain.inventory.command.domain.IdGenerator;
import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.Inbound;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.InboundRepository;

@ExtendWith(MockitoExtension.class)
class InboundServiceTest {

	@InjectMocks
	private InboundService inboundService;

	@Mock
	private LotService lotService;

	@Mock
	private InboundRepository inboundRepository;

	@Test
	@DisplayName("이미 존재하는 주문 아이템 ID인 경우 BusinessException이 발생한다")
	void receive_order_event_duplicate_fail() {
		// given
		String duplicateId = "PO_20250115_000002";
		PurchaseOrderEvent event2 = new PurchaseOrderEvent(duplicateId, "SKU_002", 200);

		given(inboundRepository.findByPurchaseOrderItemId(duplicateId))
			.willReturn(Optional.of(Inbound.builder().build())); // 이미 존재함을 가정

		// when & then
		BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(
			BusinessException.class,
			() -> inboundService.processInbound(event2)
		);

		Assertions.assertThat(exception.getMessage()).isEqualTo("이미 처리된 요청입니다.");
	}

	@Test
	@DisplayName("구매 주문이 성공적으로 처리된다")
	void receive_order_event_success() {
		// given
		PurchaseOrderEvent event1 = new PurchaseOrderEvent("PO_20250115_000001", "SKU_001", 100);

		given(inboundRepository.findByPurchaseOrderItemId(anyString()))
			.willReturn(Optional.empty());

		try (MockedStatic<IdGenerator> mockedId = mockStatic(IdGenerator.class)) {
			mockedId.when(() -> IdGenerator.createId("in")).thenReturn("IN_20260115_000001");

			// when
			inboundService.processInbound(event1);

			// then
			verify(inboundRepository, times(1)).save(any());
		}
	}
}
