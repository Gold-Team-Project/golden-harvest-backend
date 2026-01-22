package com.teamgold.goldenharvest.domain.inventory.command.application.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.inventory.command.application.dto.DiscardItemRequest;
import com.teamgold.goldenharvest.domain.inventory.command.domain.IdGenerator;
import com.teamgold.goldenharvest.domain.inventory.command.domain.discard.Discard;
import com.teamgold.goldenharvest.domain.inventory.command.domain.discard.DiscardStatus;
import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.Lot;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.DiscardRepository;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.DiscardStatusRepository;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.LotRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiscardService {

	private final DiscardRepository discardRepository;
	private final DiscardStatusRepository discardStatusRepository;
	private final LotRepository lotRepository;


	@Transactional
	public String discardItem(DiscardItemRequest discardItemRequest) {
		Lot lot = lotRepository.findByLotNo(discardItemRequest.getLotNo()).getFirst();

		if (Objects.isNull(lot)) {
			throw new BusinessException(ErrorCode.LOT_NOT_FOUND);
		}

		lot.consumeQuantity(discardItemRequest.getQuantity());

		String discardId = IdGenerator.createId("DIS");
		DiscardStatus status = discardStatusRepository.findByDiscardStatus(discardItemRequest.getDiscardStatus()).orElseThrow(
			() -> new BusinessException(ErrorCode.INVALID_DISCARD_STATUS)
		);

		Discard discard = Discard.builder()
			.discardId(discardId)
			.lotNo(lot.getLotNo())
			.discardStatus(status)
			.quantity(discardItemRequest.getQuantity())
			.discardedAt(LocalDateTime.now())
			.approvedBy(discardItemRequest.getApprovedAdminEmail())
			.discardRate(BigDecimal.valueOf(discardItemRequest.getQuantity() / lot.getQuantity()))
			.build();

		return discardRepository.save(discard).getLotNo();
	}
}
