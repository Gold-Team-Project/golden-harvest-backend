package com.teamgold.goldenharvest.domain.inventory.command.domain.mirror;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_item_master_mirror")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemMasterMirror {

	@Id
	@Column(name = "sku_no")
	private String skuNo;

	@Column(name = "item_name")
	private String itemName;

	@Column(name = "grade_name")
	private String gradeName;

	@Column(name = "variety_name")
	private String varietyName;

	@Column(name = "base_unit")
	private String baseUnit;

	@Column(name = "current_origin_price", precision = 10, scale = 2)
	private BigDecimal currentOriginPrice;

	@Column(name = "is_active")
	private boolean isActive;

	@Builder
	public ItemMasterMirror(
		String skuNo,
		String itemName,
		String gradeName,
		String varietyName,
		String baseUnit,
		BigDecimal currentOriginPrice,
		boolean isActive) {
		this.skuNo = skuNo;
		this.itemName = itemName;
		this.gradeName = gradeName;
		this.varietyName = varietyName;
		this.baseUnit = baseUnit;
		this.currentOriginPrice = currentOriginPrice;
		this.isActive = isActive;
	}

	public void updatePrice(BigDecimal updatedPrice) {
		this.currentOriginPrice = updatedPrice;
	}
}
