package com.teamgold.goldenharvest.domain.inventory.command.application.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
		String lotNo = createLotNo();

		Lot lot = Lot.builder()
			.lotNo(lotNo)
			.skuNo(purchaseOrderEvent.skuNo())
			.build(); // Todo: purchaseOrderEvent 데이터 이용하여 Lot 생성

		lotRepository.save(lot);

		return lotNo;
	}

	/*
	* Lot 번호 (native key)를 생성하는 메소드이다
	* IdGenerator는 1씩 증가하는 반드시 고유한 숫자값을 반환한다
	* */
	private String createLotNo() {
		IdGenerator generator = IdGenerator.create();
		idGeneratorRepository.save(generator);

		Long sequenceNum = generator.getId();

		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String formattedSeq = String.format("%06d", sequenceNum);

		// LOT_YYYYMMDD_NNNNNN 형식의 native id 생성
		return "LOT_" + today + "_" + formattedSeq;
	}
}
