package com.teamgold.goldenharvest.masterdata;

import com.teamgold.goldenharvest.common.exception.BusinessException;
import com.teamgold.goldenharvest.common.infra.file.domain.File;
import com.teamgold.goldenharvest.common.infra.file.service.FileUploadService;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataAppendRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.request.master.MasterDataUpdatedRequest;
import com.teamgold.goldenharvest.domain.master.command.application.dto.response.master.MasterResponse;
import com.teamgold.goldenharvest.domain.master.command.application.event.MasterDataEventPublisher;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

    @Mock
    private MasterDataEventPublisher eventPublisher; // ✅ 생성자 주입 필수

    @Test
    @DisplayName("신규 품목이면 마스터, 품종, SKU가 생성된다.")
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

        // saveAll에서 Variety는 builder로 만들어지고, 그 객체가 skuNo 생성에 쓰이므로
        // repository.save가 "저장된 variety"를 반환하도록 세팅해두면 안정적임
        Variety savedVariety = Variety.builder()
                .varietyCode("VAR001")
                .varietyName("수미")
                .produceMaster(master)
                .build();

        Grade gradeA = mock(Grade.class);
        Grade gradeB = mock(Grade.class);
        given(gradeA.getGradeCode()).willReturn("04");
        given(gradeB.getGradeCode()).willReturn("05");

        given(masterRepository.findByItemName("감자")).willReturn(Optional.empty());
        given(masterRepository.save(any(ProduceMaster.class))).willReturn(master);

        given(varietyRepository.save(any(Variety.class))).willReturn(savedVariety);

        given(gradeRepository.findByGradeCode("04")).willReturn(Optional.of(gradeA));
        given(gradeRepository.findByGradeCode("05")).willReturn(Optional.of(gradeB));

        given(skuRepository.existsBySkuNo(anyString())).willReturn(false);

        // when
        masterDataService.saveAll(List.of(response));

        // then
        verify(masterRepository, times(1)).save(any(ProduceMaster.class));
        verify(varietyRepository, times(1)).save(any(Variety.class));
        verify(skuRepository, times(2)).save(any(Sku.class)); // 04,05 두개
    }

    @Test
    @DisplayName("마스터 데이터를 추가로 등록한다. (파일 업로드 시 fileUrl 저장)")
    void appendMasterData_WithFile() throws IOException {
        // given
        ProduceMaster master = mock(ProduceMaster.class);
        MultipartFile file = mock(MultipartFile.class);

        given(file.isEmpty()).willReturn(false);
        given(masterRepository.findById("ITM001")).willReturn(Optional.of(master));

        File uploadedFile = File.builder()
                .fileUrl("https://s3.aws.com/master.png")
                .build();
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
        verify(master, times(1)).appendedMasterData(
                5, 1.0, 8.0, "저온 보관", "https://s3.aws.com/master.png"
        );
    }

    @Test
    @DisplayName("마스터 데이터를 추가로 등록한다. (파일 없으면 fileUrl=null)")
    void appendMasterData_WithoutFile() throws IOException {
        // given
        ProduceMaster master = mock(ProduceMaster.class);

        given(masterRepository.findById("ITM001")).willReturn(Optional.of(master));

        MasterDataAppendRequest request = MasterDataAppendRequest.builder()
                .shelfLifeDays(3)
                .storageTempMin(0.0)
                .storageTempMax(10.0)
                .description("상온 가능")
                .build();

        // when
        masterDataService.appendMasterData("ITM001", request, null);

        // then
        verify(fileUploadService, never()).upload(any());
        verify(master, times(1)).appendedMasterData(
                3, 0.0, 10.0, "상온 가능", null
        );
    }

    @Test
    @DisplayName("마스터가 없으면 예외가 발생한다.")
    void appendMasterData_fail_masterNotFound() {
        // given
        given(masterRepository.findById("ITM001")).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                masterDataService.appendMasterData("ITM001", mock(MasterDataAppendRequest.class), null)
        )
                .isInstanceOf(BusinessException.class);
        // 메시지는 ErrorCode 설정에 따라 달라질 수 있어서 보통 타입만 잡는 게 안전함
    }

    @Test
    @DisplayName("마스터 데이터 상태를 변경한다.")
    void updateMasterDataStatus() {
        // given
        ProduceMaster master = mock(ProduceMaster.class);
        given(masterRepository.findById("ITEM001")).willReturn(Optional.of(master));

        // when
        masterDataService.updateMasterDataStatus("ITEM001");

        // then
        verify(master, times(1)).updateMasterDataStatus();
    }

    @Test
    @DisplayName("마스터 데이터를 수정한다. (파일 업로드 시 fileUrl 저장)")
    void updatedMasterData_WithFile() throws IOException {
        // given
        ProduceMaster master = mock(ProduceMaster.class);
        MultipartFile file = mock(MultipartFile.class);

        given(file.isEmpty()).willReturn(false);
        given(masterRepository.findById("ITM001")).willReturn(Optional.of(master));

        File uploadedFile = File.builder()
                .fileUrl("https://s3.aws.com/master_updated.png")
                .build();
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
        verify(master, times(1)).updatedMasterData(
                5, 1.0, 8.0, "저온 보관", "https://s3.aws.com/master_updated.png"
        );
    }

    @Test
    @DisplayName("마스터 데이터를 수정한다. (파일 없으면 fileUrl=null)")
    void updatedMasterData_WithoutFile() throws IOException {
        // given
        ProduceMaster master = mock(ProduceMaster.class);
        given(masterRepository.findById("ITM001")).willReturn(Optional.of(master));

        MasterDataUpdatedRequest request = MasterDataUpdatedRequest.builder()
                .shelfLifeDays(7)
                .storageTempMin(2.0)
                .storageTempMax(6.0)
                .description("냉장 필수")
                .build();

        // when
        masterDataService.updatedMasterData("ITM001", request, null);

        // then
        verify(fileUploadService, never()).upload(any());
        verify(master, times(1)).updatedMasterData(
                7, 2.0, 6.0, "냉장 필수", null
        );
    }
}
