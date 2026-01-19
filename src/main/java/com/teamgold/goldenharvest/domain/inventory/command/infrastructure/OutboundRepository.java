package com.teamgold.goldenharvest.domain.inventory.command.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.Outbound;

public interface OutboundRepository extends JpaRepository<Outbound, String> { }
