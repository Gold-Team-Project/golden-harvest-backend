package com.teamgold.goldenharvest.domain.inventory.query.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.teamgold.goldenharvest.domain.inventory.query.dto.InboundResponse;

@Mapper
public interface InboundMapper {

	@Select("""
		SELECT i.inbound_id AS inboundId,
		       i.purchase_order_item_id AS purchaseOrderItemId,
		       i.sku_no AS skuNo,
		       i.quantity AS quantity,
		       i.inbound_date AS inboundDate
		FROM tb_inbound i
		WHERE 
		    (#{skuNo} IS NULL OR i.sku_no = #{skuNo})
		AND
		    (i.inbound_date BETWEEN #{startDate} AND #{endDate})
		ORDER BY i.inbound_date DESC
		LIMIT #{limit}
		OFFSET #{offset}
	""")
	List<InboundResponse> findAllInbounds(
		@Param("limit") int limit,
		@Param("offset") int offset,
		@Param("skuNo") String skuNo,
		@Param("startDate") LocalDate startDate,
		@Param("endDate") LocalDate endDate
	);
}
