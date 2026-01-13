package com.teamgold.goldenharvest.domain.purchases.command.infrastructure.repository;

import com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate.PurchaseOrder;
import com.teamgold.goldenharvest.domain.purchases.command.domain.repository.PurchaseOrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPurchaseOrderRepository extends PurchaseOrderRepository, JpaRepository<PurchaseOrder,String> {
}
