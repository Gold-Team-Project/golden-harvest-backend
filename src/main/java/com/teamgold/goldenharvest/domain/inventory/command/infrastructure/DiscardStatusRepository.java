package com.teamgold.goldenharvest.domain.inventory.command.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamgold.goldenharvest.domain.inventory.command.domain.discard.DiscardStatus;

public interface DiscardStatusRepository extends JpaRepository<DiscardStatus, String> {

	Optional<DiscardStatus> findByDiscardStatus(String discardStatus);
}
