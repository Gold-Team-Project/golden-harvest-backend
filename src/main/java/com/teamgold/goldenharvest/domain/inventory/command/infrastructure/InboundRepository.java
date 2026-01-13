package com.teamgold.goldenharvest.domain.inventory.command.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.Inbound;

@Repository
public interface InboundRepository extends JpaRepository<Inbound, String> {

	Optional<Inbound> findByPurchaseOrderItemId(String purchaseOrderItemId);
}
