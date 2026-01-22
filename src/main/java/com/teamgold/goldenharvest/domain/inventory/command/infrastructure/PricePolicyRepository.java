package com.teamgold.goldenharvest.domain.inventory.command.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.PricePolicy;

public interface PricePolicyRepository extends JpaRepository<PricePolicy, String> {
	List<PricePolicy> findBySkuNo(String skuNo);
}
