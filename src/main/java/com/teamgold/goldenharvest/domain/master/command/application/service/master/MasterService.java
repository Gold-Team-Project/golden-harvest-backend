package com.teamgold.goldenharvest.domain.master.command.application.service.master;

import com.teamgold.goldenharvest.domain.master.command.application.dto.response.master.MasterResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MasterService {
    //품목 정보 등록
    void saveAll(List<MasterResponse> responses);
    //품목 비활성화

}
