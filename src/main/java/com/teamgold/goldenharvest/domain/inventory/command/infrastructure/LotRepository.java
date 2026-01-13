package com.teamgold.goldenharvest.domain.inventory.command.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.Lot;

@Repository
public interface LotRepository extends JpaRepository<Lot, String> { }
