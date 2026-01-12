package com.teamgold.goldenharvest.domain.purchases.command.infrastructure.repository;

import com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate.PurchaseOrderItem;
import com.teamgold.goldenharvest.domain.purchases.command.domain.repository.PurchaseOrderItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPurchaseOrderItemRepository extends PurchaseOrderItemRepository, JpaRepository<PurchaseOrderItem,Long> {
}
