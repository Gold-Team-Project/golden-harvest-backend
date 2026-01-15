package com.teamgold.goldenharvest.domain.inventory.command.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamgold.goldenharvest.domain.inventory.command.domain.mirror.ItemMasterMirror;

@Repository
public interface ItemMasterMirrorRepository extends JpaRepository<ItemMasterMirror, String> { }
