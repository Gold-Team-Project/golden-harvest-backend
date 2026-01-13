package com.teamgold.goldenharvest.domain.inventory.command.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamgold.goldenharvest.domain.inventory.command.domain.IdGenerator;

public interface IdGeneratorRepository extends JpaRepository<IdGenerator, String> { }
