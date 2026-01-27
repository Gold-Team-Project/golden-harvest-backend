package com.teamgold.goldenharvest.domain.inventory.query.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.teamgold.goldenharvest.domain.inventory.query.dto.DiscardLossResponse;
import com.teamgold.goldenharvest.domain.inventory.query.dto.DiscardRateResponse;
import com.teamgold.goldenharvest.domain.inventory.query.dto.DiscardResponse;
import com.teamgold.goldenharvest.domain.inventory.query.dto.DiscardVolumeResponse;

@Mapper
public interface DiscardMapper {

	@Select("""
		SELECT
		    d.discard_id AS discardId,
		    d.lot_no AS lotNo,
		    d.quantity AS quantity,
		    d.discarded_at AS discardedAt,
		    d.approved_by AS approvedEmailId,
		    d.discard_status AS discardStatus,
		    d.discard_rate AS discardRate
		FROM
		    tb_discard AS d
		JOIN
			tb_lot AS l
		ON
			l.lot_no = d.lot_no
		WHERE
		    (#{skuNo} IS NULL OR l.sku_no = #{skuNo})
		    AND
		    (#{discardStatus} IS NULL OR d.discard_status = #{discardStatus})
		    AND
		    (d.discarded_at BETWEEN #{startDate} AND #{endDate})
		ORDER BY d.discarded_at DESC
		LIMIT #{limit}
		OFFSET #{offset}
	""")
	List<DiscardResponse> findAllDiscard(
		@Param("limit") Integer limit,
		@Param("offset") Integer offset,
		@Param("skuNo") String skuNo,
		@Param("discardStatus") String discardStatus,
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate
	);

	@Select("""
		SELECT
		    COALESCE(SUM(CASE WHEN discarded_at BETWEEN #{thisMonthStart} AND #{now} THEN quantity ELSE 0 END), 0) AS currentQuantity,
		    COALESCE(SUM(CASE WHEN discarded_at BETWEEN #{lastMonthStart} AND #{lastMonthUntilNow} THEN quantity ELSE 0 END), 0) AS lastQuantity
      	FROM tb_discard
      	WHERE discarded_at BETWEEN #{lastMonthStart} AND #{now}
	""")
	DiscardVolumeResponse findDiscardVolume(
		@Param("lastMonthStart") LocalDateTime lastMonthStart,
		@Param("lastMonthUntilNow") LocalDateTime lastMonthUntilNow,
		@Param("thisMonthStart") LocalDateTime thisMonthStart,
		@Param("now") LocalDateTime now
	);

	@Select("""
		SELECT
		    COALESCE(SUM(CASE WHEN discarded_at BETWEEN #{thisMonthStart} AND #{now} THEN price ELSE 0 END), 0) AS currentTotalValue,
			COALESCE(SUM(CASE WHEN discarded_at BETWEEN #{lastMonthStart} AND #{lastMonthUntilNow} THEN price ELSE 0 END), 0) AS lastTotalValue
      	FROM tb_discard
      	WHERE discarded_at BETWEEN #{lastMonthStart} AND #{now}
	""")
	DiscardLossResponse findDiscardLoss(
		@Param("lastMonthStart") LocalDateTime lastMonthStart,
		@Param("lastMonthUntilNow") LocalDateTime lastMonthUntilNow,
		@Param("thisMonthStart") LocalDateTime thisMonthStart,
		@Param("now") LocalDateTime now
	);

	@Select("""
		SELECT
		    SUM(d. quantity) AS totalQuantity,
		    i.item_name AS itemName
		FROM 
		    tb_discard AS d
		JOIN 
			tb_lot l
		ON 
		    l.lot_no = d.lot_no
		JOIN
		    tb_item_master_mirror i
		ON
			l.sku_no = i.sku_no
		WHERE
		    d.discarded_at BETWEEN #{thisMonthStart} AND #{now}
		GROUP BY
		    i.item_name
		ORDER BY
			totalQuantity DESC
		LIMIT 10
	""")
	List<DiscardRateResponse> findDiscardRate(
		@Param("thisMonthStart") LocalDateTime thisMonthStart,
		@Param("now") LocalDateTime now
	);
}
