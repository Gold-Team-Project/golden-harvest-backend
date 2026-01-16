package com.teamgold.goldenharvest.domain.inventory.command.domain.lot;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_price_policy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PricePolicy {

	@Id
	@Column(name = "sku_no")
	private String skuNo;

	@DecimalMin(value = "0.0", inclusive = true)
	@DecimalMax(value = "1.0", inclusive = true)
	@Column(name = "margin_rate", precision = 10, scale = 2, nullable = false)
	private BigDecimal marginRate;

	@Column(name = "is_active")
	private Boolean isActive;

	@Builder
	public PricePolicy(
		String skuNo,
		BigDecimal marginRate) {
		this.skuNo = skuNo;
		this.marginRate = marginRate;
		this.isActive = true;
	}

	public void activate() {
		this.isActive = true;
	}

	public void deactivate() {
		this.isActive = false;
	}
}
