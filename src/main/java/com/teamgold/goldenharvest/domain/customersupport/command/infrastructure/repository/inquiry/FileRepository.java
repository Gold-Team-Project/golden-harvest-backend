package com.teamgold.goldenharvest.domain.customersupport.command.infrastructure.repository.inquiry;

import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}
