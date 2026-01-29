package com.teamgold.goldenharvest.common.infra.file.infrastucture;

import com.teamgold.goldenharvest.common.infra.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileUrl(String fileUrl);
}
