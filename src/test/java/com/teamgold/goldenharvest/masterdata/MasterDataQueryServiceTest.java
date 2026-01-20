package com.teamgold.goldenharvest.masterdata;

import com.teamgold.goldenharvest.domain.master.query.dto.response.master.MasterDataDetailResponse;
import com.teamgold.goldenharvest.domain.master.query.dto.response.master.MasterDataListResponse;
import com.teamgold.goldenharvest.domain.master.query.dto.response.price.OriginPriceResponse;
import com.teamgold.goldenharvest.domain.master.query.mapper.MasterMapper;
import com.teamgold.goldenharvest.domain.master.query.service.impl.MasterQueryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MasterDataQueryServiceTest {
    @InjectMocks
    MasterQueryServiceImpl masterQueryService;

    @Mock
    MasterMapper masterMapper;

    @Test
    @DisplayName("마스터 데이터 목록을 페이징과 조건으로 조회한다")
    void getAllMasterData_success() {
        // given
        int page = 2;
        int size = 10;

        String itemName = "감자";
        String itemCode = "ITM001";
        String varietyName = "수미";
        String gradeName = "특";
        Boolean isActive = true;

        int expectedLimit = 10;
        int expectedOffset = 10;

        List<MasterDataListResponse> list =
                List.of(mock(MasterDataListResponse.class));

        given(masterMapper.findAllMasterData(
                itemName,
                itemCode,
                varietyName,
                gradeName,
                isActive,
                expectedLimit,
                expectedOffset
        )).willReturn(list);
        // when
        List<MasterDataListResponse> result =
                masterQueryService.getAllMasterData(
                        page, size,
                        itemName, itemCode,
                        varietyName, gradeName,
                        isActive
                );
        // then
        assertThat(result).hasSize(1);
        verify(masterMapper).findAllMasterData(
                itemName,
                itemCode,
                varietyName,
                gradeName,
                isActive,
                expectedLimit,
                expectedOffset
        );
    }
    @Test
    @DisplayName("마스터 상세 조회시 최근 원가 목록을 함께세팅한다")
    void getDetailMasterData_success() {
        // given
        String skuNo = "SKU-001";

        MasterDataDetailResponse detail =
                new MasterDataDetailResponse();

        List<OriginPriceResponse> prices =
                List.of(mock(OriginPriceResponse.class));

        given(masterMapper.findDetailMasterData(skuNo))
                .willReturn(detail);

        given(masterMapper.findRecentOriginPrices(skuNo))
                .willReturn(prices);

        // when
        MasterDataDetailResponse result =
                masterQueryService.getDetailMasterData(skuNo);

        // then
        assertThat(result.getOriginPrices()).isEqualTo(prices);

        verify(masterMapper).findDetailMasterData(skuNo);
        verify(masterMapper).findRecentOriginPrices(skuNo);
    }
    @Test
    @DisplayName("최근 원가가 없어도 상세 조회는 정상 동작한다")
    void getDetailMasterData_without_prices() {
        // given
        String skuNo = "SKU-002";

        MasterDataDetailResponse detail =
                new MasterDataDetailResponse();

        given(masterMapper.findDetailMasterData(skuNo))
                .willReturn(detail);

        given(masterMapper.findRecentOriginPrices(skuNo))
                .willReturn(List.of());

        // when
        MasterDataDetailResponse result =
                masterQueryService.getDetailMasterData(skuNo);

        // then
        assertThat(result.getOriginPrices()).isEmpty();
    }


}
