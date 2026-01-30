package com.teamgold.goldenharvest.domain.inventory.command.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.inventory.command.application.dto.DiscardItemRequest;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.IdGenerator;
import com.teamgold.goldenharvest.domain.inventory.command.domain.discard.Discard;
import com.teamgold.goldenharvest.domain.inventory.command.domain.discard.DiscardStatus;
import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.Lot;
import com.teamgold.goldenharvest.domain.inventory.command.domain.mirror.ItemMasterMirror;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.DiscardRepository;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.DiscardStatusRepository;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.ItemMasterMirrorRepository;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.LotRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiscardService {

	private final DiscardRepository discardRepository;
	private final DiscardStatusRepository discardStatusRepository;
	private final LotRepository lotRepository;
	private final ItemMasterMirrorRepository itemMasterMirrorRepository;


	@Transactional
	public String discardItem(DiscardItemRequest discardItemRequest) {
		Lot lot = lotRepository.findByLotNo(discardItemRequest.getLotNo()).getFirst();

		if (Objects.isNull(lot)) {
			throw new BusinessException(ErrorCode.LOT_NOT_FOUND);
		}

		lot.consumeQuantity(discardItemRequest.getQuantity());

		String discardId = IdGenerator.createId("DIS");
		DiscardStatus status = discardStatusRepository.findByDiscardStatus(discardItemRequest.getDiscardStatus())
				.orElseThrow(
			() -> new BusinessException(ErrorCode.INVALID_DISCARD_STATUS)
		);

		ItemMasterMirror itemMaster = itemMasterMirrorRepository.findById(lot.getSkuNo()).orElseThrow(
			() -> new BusinessException(ErrorCode.NO_SUCH_SKU)
		);

		Discard discard = Discard.builder()
			.discardId(discardId)
			.lotNo(lot.getLotNo())
			.discardStatus(status)
			.quantity(discardItemRequest.getQuantity())
			.discardedAt(LocalDateTime.now())
			.approvedBy("sampleuser@sample.com")
			.discardRate(BigDecimal.valueOf(discardItemRequest.getQuantity() / lot.getQuantity()))
			.build();

		BigDecimal originPrice = Optional.ofNullable(itemMaster.getCurrentOriginPrice())
				.orElse(BigDecimal.ZERO);

		discard.updateTotalPrice(originPrice);

		return discardRepository.save(discard).getLotNo();
	}
}
