package com.teamgold.goldenharvest.domain.inventory.command.application.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamgold.goldenharvest.domain.inventory.command.application.dto.PurchaseOrderEvent;
import com.teamgold.goldenharvest.domain.inventory.command.domain.IdGenerator;
import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.Lot;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.IdGeneratorRepository;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.LotRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LotService {

	private final LotRepository lotRepository;
	private final IdGeneratorRepository idGeneratorRepository;

	@Transactional
	public String createLot(PurchaseOrderEvent purchaseOrderEvent) {
		String lotNo = IdGenerator.createId("lot");

		Lot lot = Lot.builder()
			.lotNo(lotNo)
			.skuNo(purchaseOrderEvent.skuNo())
			.build(); // Todo: purchaseOrderEvent 데이터 이용하여 Lot 생성

		lotRepository.save(lot);

		return lotNo;
	}

	@Transactional
	public List<String> OutboundLot(String skuNo, Integer quantity) {
		// Todo: FIFO에 맞게 주문 들어온 상품을 출고 (수량 및 상태 변경 등)
		// Todo: 변경이 발생한 lotNo들을 모두 반환
		return null;
	}
}
