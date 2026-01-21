package com.teamgold.goldenharvest.domain.inventory.query.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.teamgold.goldenharvest.domain.inventory.query.dto.DiscardResponse;

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
}
