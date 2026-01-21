package com.teamgold.goldenharvest.domain.inventory.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.teamgold.goldenharvest.domain.inventory.query.dto.PricePolicyResponse;

@Mapper
public interface PricePolicyMapper {

	@Select("""
		SELECT p.sku_no AS skuNo,
		       p.margin_rate AS marginRate,
		       p.is_active AS isActive
		FROM tb_price_policy p
	""")
	List<PricePolicyResponse> findAllPricePolicies();
}
