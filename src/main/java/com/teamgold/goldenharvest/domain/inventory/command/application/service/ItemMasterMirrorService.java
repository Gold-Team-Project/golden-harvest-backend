package com.teamgold.goldenharvest.domain.inventory.command.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.teamgold.goldenharvest.domain.inventory.command.application.dto.ItemMasterUpdatedData;
import com.teamgold.goldenharvest.domain.inventory.command.domain.mirror.ItemMasterMirror;
import com.teamgold.goldenharvest.domain.inventory.command.infrastructure.ItemMasterMirrorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemMasterMirrorService {

	private final ItemMasterMirrorRepository itemMasterMirrorRepository;

	public void updateItemMasterMirror(List<ItemMasterUpdatedData> updatedDataList) {
		List<ItemMasterMirror> updatedEntityList = updatedDataList.stream().map(
			data -> ItemMasterMirror.builder()
				.skuNo(data.skuNo())
				.itemName(data.itemName())
				.varietyName(data.varietyName())
				.gradeName(data.gradeName())
				.baseUnit(data.baseUnit())
				.isActive(data.isActive())
				.build()
		).toList();

		itemMasterMirrorRepository.saveAll(updatedEntityList);
	}
}
