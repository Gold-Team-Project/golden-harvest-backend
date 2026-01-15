package com.teamgold.goldenharvest.inventory;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.teamgold.goldenharvest.domain.inventory.query.dto.AvailableItemResponse;
import com.teamgold.goldenharvest.domain.inventory.query.mapper.LotMapper;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실 DB가 아닌 설정된 H2 사용
@ActiveProfiles("test") // application-test.yml 적용
class LotQueryMapperTest {

	@Autowired
	LotMapper lotMapper;

	@Test
	@Sql(scripts = {"/test-sql/lot-query-schema.sql", "/test-sql/lot-query-data.sql"})
	@DisplayName("사용자 대상 재고 조회가 된다 (쿼리)")
	void find_all_item() {
		// Given: resources/test-sql 내 sql 쿼리로 주어짐
		// When
		List<AvailableItemResponse> responses = lotMapper.findAllAvailableItems(10, 0);

		// Then
		Assertions.assertThat(responses.getFirst().getSkuNo()).isEqualTo("SKU001");
		Assertions.assertThat(responses.getFirst().getCustomerPrice()).isBetween(11499d, 11501d);
	}
}
