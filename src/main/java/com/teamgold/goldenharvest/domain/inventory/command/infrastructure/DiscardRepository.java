package com.teamgold.goldenharvest.domain.inventory.command.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamgold.goldenharvest.domain.inventory.command.domain.discard.Discard;

public interface DiscardRepository extends JpaRepository<Discard, String> {
}
