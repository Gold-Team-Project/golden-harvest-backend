package com.teamgold.goldenharvest.inventory;

import com.teamgold.goldenharvest.domain.inventory.query.dto.AvailableItemResponse;
import com.teamgold.goldenharvest.domain.inventory.query.dto.InboundResponse;
import com.teamgold.goldenharvest.domain.inventory.query.dto.OutboundResponse;
import com.teamgold.goldenharvest.domain.inventory.query.mapper.InboundMapper;
import com.teamgold.goldenharvest.domain.inventory.query.mapper.LotMapper;
import com.teamgold.goldenharvest.domain.inventory.query.mapper.OutboundMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실 DB가 아닌 설정된 H2 사용
@ActiveProfiles("test") // application-test.yml 적용, given: ddl 및 더미데이터 삽입
@MapperScan("com.teamgold.goldenharvest.domain.inventory.query.mapper")
class LotQueryMapperTest {

	@Autowired
	LotMapper lotMapper;

	@Autowired
	InboundMapper inboundMapper;

	@Autowired
	OutboundMapper outboundMapper;

	@Test
	@DisplayName("사용자 대상 재고 조회 쿼리가 정상적으로 작동된다")
	void find_all_item() {
		// when
		List<AvailableItemResponse> responses = lotMapper.findAllAvailableItems(10, 0, null);

		// then
		Assertions.assertThat(responses.getFirst().getSkuNo()).isEqualTo("SKU_20260120_000001");
		Assertions.assertThat(responses.getFirst().getCustomerPrice()).isBetween(34499d, 34501d);
	}

	@Test
	@DisplayName("입고(Inbound) 조회 쿼리가 정상적으로 작동된다")
	void find_all_inbounds() {
		// when
		List<InboundResponse> skuFilteredResponses = inboundMapper.findAllInbounds(
			10,
			0,
			"SKU_20260120_000003",
			LocalDate.now().minusMonths(2),
			LocalDate.now()
		);

		List<InboundResponse> dateFilteredResponses = inboundMapper.findAllInbounds(
			10,
			0,
			null,
			LocalDate.of(2026, 1, 19),
			LocalDate.now()
		);

		// then
		Assertions.assertThat(skuFilteredResponses.getFirst().getSkuNo()).isEqualTo("SKU_20260120_000003");
		Assertions.assertThat(dateFilteredResponses).hasSize(2);
	}

	@Test
	@DisplayName("출고(outbound) 조회 쿼리가 정상적으로 작동된다")
	void find_all_outbounds() {
		// when
		List<OutboundResponse> lotFilteredResponses = outboundMapper.findAllOutbounds(
			10,
			0,
			null,
			"LOT_20260120_000003",
			LocalDate.now().minusMonths(2),
			LocalDate.now()
		);

		List<OutboundResponse> dateFilteredResponses = outboundMapper.findAllOutbounds(
			10,
			0,
			null,
			null,
			LocalDate.of(2026, 1, 20),
			LocalDate.now()
		);

		// then
		Assertions.assertThat(lotFilteredResponses).hasSize(1);
		Assertions.assertThat(lotFilteredResponses.getFirst().getLotNo()).isEqualTo("LOT_20260120_000003");

		Assertions.assertThat(dateFilteredResponses).hasSize(1);
	}
}
