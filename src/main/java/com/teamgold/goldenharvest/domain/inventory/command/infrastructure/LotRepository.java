package com.teamgold.goldenharvest.domain.inventory.command.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.Lot;
import com.teamgold.goldenharvest.domain.inventory.command.domain.lot.LotStatus;

import jakarta.persistence.LockModeType;

@Repository
public interface LotRepository extends JpaRepository<Lot, String> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<Lot> findBySkuNoAndLotStatusOrderByInboundDateAsc(String skuNo, LotStatus.LotStatusType lotStatus);
}
