package com.teamgold.goldenharvest.domain.inventory.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.teamgold.goldenharvest.domain.inventory.query.dto.AvailableItemResponse;

@Mapper
public interface LotMapper {

	@Select("""
		SELECT
		    l.sku_no AS skuNo,
		    SUM(l.quantity) AS quantity,
		    i.item_name AS itemName,
		    i.grade_name AS gradeName,
		    i.variety_name AS varietyName,
		    i.base_unit AS baseUnit,
		    ROUND(i.current_origin_price * COALESCE(1 + p.margin_rate, 1.2), 0) AS customerPrice
		FROM
		    tb_lot l
		JOIN
		    tb_item_master_mirror i
		ON
		    l.sku_no = i.sku_no
		LEFT JOIN
		    tb_price_policy p
		ON
			l.sku_no = p.sku_no
		WHERE
		    l.lot_status = 'AVAILABLE'
		GROUP BY
		    l.sku_no,
          	i.item_name,
          	i.grade_name,
          	i.variety_name,
          	i.base_unit,
	        i.current_origin_price,
         	p.margin_rate
		ORDER BY
		    i.item_name,
		    l.sku_no
		LIMIT
		    #{limit}
		OFFSET
		    #{offset}
	"""
	)
	List<AvailableItemResponse> findAllAvailableItems(@Param("limit") int limit, @Param("offset") int offset);
}
