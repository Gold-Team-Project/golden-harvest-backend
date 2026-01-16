package com.teamgold.goldenharvest.common.infra.file.repository;

import com.teamgold.goldenharvest.common.infra.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}
