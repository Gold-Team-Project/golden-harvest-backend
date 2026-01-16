package com.teamgold.goldenharvest.domain.purchases.command.domain.repository;

import com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate.PurchaseOrder;

public interface PurchaseOrderRepository {

    PurchaseOrder save(PurchaseOrder purchaseOrder);

}
