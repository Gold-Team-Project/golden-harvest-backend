package com.teamgold.goldenharvest.domain.master.command.infrastucture.mater;

import com.teamgold.goldenharvest.domain.master.command.domain.master.ProduceMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MasterRepository extends JpaRepository<ProduceMaster, String> {
    Optional<ProduceMaster> findByItemName(String itemName);
}
