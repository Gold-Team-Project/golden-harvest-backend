package com.teamgold.goldenharvest.domain.inventory.query.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.teamgold.goldenharvest.domain.inventory.query.dto.OutboundResponse;

@Mapper
public interface OutboundMapper {

	@Select("""
		SELECT 
		    o.outbound_id AS outboundId,
		    o.sales_order_item_id AS salesOrderItemId,
		    o.lot_no AS lotNo,
		    l.sku_no AS skuNo,
		    o.outbound_date AS outboundDate,
		    o.quantity AS quantity,
		    o.outbound_price AS outboundPrice
		FROM
		    tb_outbound AS o
		JOIN
			tb_lot AS l
		ON o.lot_no = l.lot_no
		WHERE 
		    (#{skuNo} IS NULL OR l.sku_no = #{skuNo})
		AND
		    (#{lotNo} IS NULL OR l.lot_no = #{lotNo})
		AND
		    (o.outbound_date BETWEEN #{startDate} AND #{endDate})
		ORDER BY o.outbound_date DESC
		LIMIT #{limit}
		OFFSET #{offset}
	""")
	List<OutboundResponse> findAllOutbounds(
		@Param("limit") int limit,
		@Param("offset") int offset,
		@Param("skuNo") String skuNo,
		@Param("lotNo") String lotNo,
		@Param("startDate") LocalDate startDate,
		@Param("endDate") LocalDate endDate
	);
}
