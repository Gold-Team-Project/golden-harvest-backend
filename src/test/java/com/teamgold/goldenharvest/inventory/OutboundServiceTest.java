package com.teamgold.goldenharvest.inventory;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.sales.command.application.event.dto.SalesOrderEvent;
import com.teamgold.goldenharvest.domain.inventory.command.application.service.LotService;
import com.teamgold.goldenharvest.domain.inventory.command.application.service.OutboundService;
import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.Lot;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.LotRepository;

@ExtendWith(MockitoExtension.class)
class OutboundServiceTest {

	@InjectMocks
	private LotService lotService; // ConsumeLot이 포함된 서비스 클래스명으로 변경하세요

	@Mock
	private LotRepository lotRepository;

	@Mock
	private OutboundService outboundService;

	@Test
	@DisplayName("재고가 충분할 때 FIFO 순서대로 재고를 소진한다")
	void consume_lot_success() {
		// given
		String salesOrderItemId = "SO_001";
		String skuNo = "SKU_001";
		BigDecimal salesPrice = BigDecimal.valueOf(10_000);
		int orderQuantity = 150;
		SalesOrderEvent event = new SalesOrderEvent(salesOrderItemId,skuNo, salesPrice, orderQuantity);

		// Lot 1: 100개, Lot 2: 100개 준비 (FIFO 순서대로 리스트 구성)
		String lotNo1 = "LOT_001";
		String lotNo2 = "LOT_002";

		Lot lot1 = spy(new Lot(
			lotNo1,
			"IN_001",
			skuNo,
			100,
			LocalDate.of(2026, 1, 1),
			Lot.LotStatus.AVAILABLE));

		Lot lot2 = spy(new Lot(
			lotNo2,
			"IN_002",
			skuNo,
			100,
			LocalDate.of(2026, 1, 2),
			Lot.LotStatus.AVAILABLE));

		given(lotRepository.findBySkuNoAndLotStatusOrderByInboundDateAsc(skuNo, Lot.LotStatus.AVAILABLE))
			.willReturn(List.of(lot1, lot2));

		// when
		lotService.consumeLot(event);

		// then
		verify(lot1).consumeQuantity(150);
		verify(lot2).consumeQuantity(50);

		verify(outboundService).processOutbound(eq(lot1), eq(100), any());
		verify(outboundService).processOutbound(eq(lot2), eq(50), any());

		assertThat(lot1.getQuantity()).isZero();
		assertThat(lot2.getQuantity()).isEqualTo(50);
	}

	@Test
	@DisplayName("전체 재고 수량이 주문 수량보다 적으면 BusinessException이 발생한다")
	void consume_insuf_stock_fail() {
		// given
		String salesOrderItemId = "SO_001";
		String skuNo = "SKU_001";
		BigDecimal salesPrice = BigDecimal.valueOf(10_000);
		int orderQuantity = 250;
		SalesOrderEvent event = new SalesOrderEvent(salesOrderItemId,skuNo, salesPrice, orderQuantity);

		String lotNo1 = "LOT_001";
		String lotNo2 = "LOT_002";

		Lot lot1 = spy(new Lot(
			lotNo1,
			"IN_001",
			skuNo,
			100,
			LocalDate.of(2026, 1, 1),
			Lot.LotStatus.AVAILABLE));

		Lot lot2 = spy(new Lot(
			lotNo2,
			"IN_002",
			skuNo,
			100,
			LocalDate.of(2026, 1, 2),
			Lot.LotStatus.AVAILABLE));

		given(lotRepository.findBySkuNoAndLotStatusOrderByInboundDateAsc(skuNo, Lot.LotStatus.AVAILABLE))
			.willReturn(List.of(lot1, lot2));

		// when & then
		BusinessException exception = assertThrows(BusinessException.class, () -> {
			lotService.consumeLot(event);
		});

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INSUFFICIENT_STOCK);

		// 재고 부족 시 실제 소진 로직(outbound)은 호출되지 않아야 함
		verify(outboundService, never()).processOutbound(any(), anyInt(), any());
	}
}
