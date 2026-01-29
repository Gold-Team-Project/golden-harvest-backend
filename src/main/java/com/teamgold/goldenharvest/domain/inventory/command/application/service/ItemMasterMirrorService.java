package com.teamgold.goldenharvest.domain.inventory.command.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.exception.ErrorCode;
import com.teamgold.goldenharvest.domain.master.command.application.event.dto.ItemMasterUpdatedEvent;
import com.teamgold.goldenharvest.domain.master.command.application.event.dto.ItemOriginPriceUpdateEvent;
import com.teamgold.goldenharvest.domain.inventory.command.domain.mirror.ItemMasterMirror;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.ItemMasterMirrorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemMasterMirrorService {

	private final ItemMasterMirrorRepository itemMasterMirrorRepository;

	public void updateItemMasterMirror(ItemMasterUpdatedEvent itemMasterUpdatedEvent) {
		ItemMasterMirror itemMasterMirror = ItemMasterMirror.builder()
				.skuNo(itemMasterUpdatedEvent.skuNo())
				.itemName(itemMasterUpdatedEvent.itemName())
				.gradeName(itemMasterUpdatedEvent.gradeName())
				.varietyName(itemMasterUpdatedEvent.varietyName())
				.baseUnit(itemMasterUpdatedEvent.baseUnit())
				.isActive(itemMasterUpdatedEvent.isActive())
				.fileUrl(itemMasterUpdatedEvent.fileUrl())
				.build();

		itemMasterMirrorRepository.save(itemMasterMirror);
	}

	public void updateOriginPrice(ItemOriginPriceUpdateEvent itemOriginPriceUpdateEvent) {
		List<ItemMasterMirror> itemMasterMirrors = itemMasterMirrorRepository.findBySkuNo(
			itemOriginPriceUpdateEvent.skuNo());

//		if (!itemOriginPriceUpdateEvent.updatedDate().equals(LocalDate.now())) {
//			throw new BusinessException(ErrorCode.INVALID_REQUEST);
//		} // 원가 정보가 당일 데이터가 아닌 경우 오류

		if (itemMasterMirrors.size() != 1) {
			throw new BusinessException(ErrorCode.INVALID_REQUEST);
		} // 원가 정보 업데이트 대상 상품이 1개가 아닐 경우 오류

		ItemMasterMirror itemMasterMirror = itemMasterMirrors.getFirst();

		itemMasterMirror.updatePrice(itemOriginPriceUpdateEvent.originPrice());
	}
}
