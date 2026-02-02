package com.teamgold.goldenharvest.inventory;

import com.teamgold.goldenharvest.domain.inventory.query.dto.AvailableItemResponse;
import com.teamgold.goldenharvest.domain.inventory.query.mapper.LotMapper;
import com.teamgold.goldenharvest.domain.inventory.query.service.InventoryQueryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class LotQueryServiceTest {

	@Mock
	private LotMapper lotMapper;

	@InjectMocks
	private InventoryQueryService lotQueryService;

	@Test
	@DisplayName("사용자 대상 재고 조회가 된다 (서비스)")
	void read_customer_item_list() {
		// given
		List<AvailableItemResponse> givenResponses = new ArrayList<>();

		givenResponses.add(AvailableItemResponse.builder()
				.baseUnit("kg")
				.customerPrice(10_000.0)
				.skuNo("testsku01")
				.gradeName("상급")
				.varietyName("variety01")
				.itemName("item01")
				.quantity(10_000)
			.build());

		// lotMapper의 동작 mock
		given(lotMapper.findAllAvailableItems(1, 0, null)).willReturn(givenResponses);

		// when
		List<AvailableItemResponse> receivedResponses = lotQueryService.getAllAvailableItem(1, 1, null);
		AvailableItemResponse givenResponse = givenResponses.getFirst();
		AvailableItemResponse receivedResponse = receivedResponses.getFirst();

		// then
		Assertions.assertThat(givenResponse.getSkuNo()).isEqualTo(receivedResponse.getSkuNo());
		Assertions.assertThat(givenResponse.getGradeName()).isEqualTo(receivedResponse.getGradeName());
		// 이하 생략
	}
}
