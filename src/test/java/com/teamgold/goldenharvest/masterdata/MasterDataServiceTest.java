package com.teamgold.goldenharvest.masterdata;


import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.infra.file.service.FileUploadService;
import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.File;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataAppendRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataUpdatedRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.response.master.MasterResponse;
import com.teamgold.goldenharvest.domain.master.command.application.service.master.impl.MasterDataServiceImpl;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Grade;
import com.teamgold.goldenharvest.domain.master.command.domain.master.ProduceMaster;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Sku;
import com.teamgold.goldenharvest.domain.master.command.domain.master.Variety;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.GradeRepository;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.MasterRepository;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.SkuRepository;
import com.teamgold.goldenharvest.domain.master.command.infrastucture.mater.VarietyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MasterDataServiceTest {
    @InjectMocks
    private MasterDataServiceImpl masterDataService;

    @Mock
    private MasterRepository masterRepository;
    @Mock
    private VarietyRepository varietyRepository;
    @Mock
    private GradeRepository gradeRepository;
    @Mock
    private SkuRepository skuRepository;
    @Mock
    private FileUploadService fileUploadService;


    @Test
    @DisplayName("신규 품목이면 마스터, 품종, SKU가 생성한다.")
    void saveAllMasterData() {
        // given
        MasterResponse response = MasterResponse.builder()
                .itemName("감자")
                .itemCode("ITM001")
                .baseUnit("KG")
                .unitSize("1")
                .countryName("국내")
                .kindCode("VAR001")
                .kindName("수미")
                .productRank("04,05")
                .build();

        ProduceMaster master = ProduceMaster.builder()
                .itemCode("ITM001")
                .itemName("감자")
                .baseUnit("KG")
                .country("국내")
                .isActive(false)
                .build();

        Variety variety = mock(Variety.class);
        Grade gradeA = mock(Grade.class);
        Grade gradeB = mock(Grade.class);

        given(masterRepository.findByItemName("감자")).willReturn(Optional.empty());
        given(masterRepository.save(any(ProduceMaster.class))).willReturn(master);
        given(varietyRepository.save(any(Variety.class))).willReturn(variety);
        given(gradeRepository.findByGradeCode("04")).willReturn(Optional.of(gradeA));
        given(gradeRepository.findByGradeCode("05")).willReturn(Optional.of(gradeB));
        given(skuRepository.existsBySkuNo(anyString())).willReturn(false);

        // when
        masterDataService.saveAll(List.of(response));

        // then
        verify(masterRepository, times(1)).save(any(ProduceMaster.class));

        verify(varietyRepository, times(1)).save(any(Variety.class));
        //sku 등급별로 save (04,05)
        verify(skuRepository, times(2)).save(any(Sku.class));
    }

    @Test
    @DisplayName("마스터 데이터를 추가로 등록한다.")
    void appendMasterData() throws IOException {
        // given
        ProduceMaster master = mock(ProduceMaster.class);
        MultipartFile file = mock(MultipartFile.class);

        given(file.isEmpty()).willReturn(false);
        given(masterRepository.findById("ITM001")).willReturn(Optional.of(master));

        var uploadedFile = mock(File.class);
        given(uploadedFile.getFileId()).willReturn(100L);
        given(fileUploadService.upload(file)).willReturn(uploadedFile);

        MasterDataAppendRequest request = MasterDataAppendRequest.builder()
                .shelfLifeDays(5)
                .storageTempMin(1.0)
                .storageTempMax(8.0)
                .description("저온 보관")
                .build();
        // when
        masterDataService.appendMasterData("ITM001", request, file);
        // then
        verify(fileUploadService, times(1)).upload(file);
        verify(master).appendedMasterData(5, 1.0, 8.0, "저온 보관", 100L);
    }

    @Test
    @DisplayName("마스터가 없으면 예외가 발생한다.")
    void MasterData_fail() {
        // given
        given(masterRepository.findById("ITM001")).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                masterDataService.appendMasterData(
                        "ITM001",
                        mock(MasterDataAppendRequest.class),
                        null
                )
        ).isInstanceOf(BusinessException.class)
                .hasMessageContaining("마스터 데이터를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("마스터 데이터 상태를 변경한다.")
    void updateMasterDataStatus() {
        //given
        ProduceMaster master = mock(ProduceMaster.class);
        given(masterRepository.findById("ITEM001")).willReturn(Optional.of(master));
        //when
        masterDataService.updateMasterDataStatus("ITEM001");
        //then
        verify(master,times(1)).updateMasterDataStatus();
    }

    @Test
    @DisplayName("마스터 데이터를 수정한다.")
    void updatedMasterData() throws IOException {
        // given
        ProduceMaster master = mock(ProduceMaster.class);
        MultipartFile file = mock(MultipartFile.class);

        given(file.isEmpty()).willReturn(false);
        given(masterRepository.findById("ITM001")).willReturn(Optional.of(master));

        var uploadedFile = mock(File.class);
        given(uploadedFile.getFileId()).willReturn(100L);
        given(fileUploadService.upload(file)).willReturn(uploadedFile);

        MasterDataUpdatedRequest request = MasterDataUpdatedRequest.builder()
                .shelfLifeDays(5)
                .storageTempMin(1.0)
                .storageTempMax(8.0)
                .description("저온 보관")
                .build();
        // when
        masterDataService.updatedMasterData("ITM001", request, file);
        // then
        verify(fileUploadService, times(1)).upload(file);
        verify(master).updatedMasterData(5, 1.0, 8.0, "저온 보관", 100L);
    }
}
