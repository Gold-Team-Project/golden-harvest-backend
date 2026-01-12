package com.teamgold.goldenharvest.domain.purchases.command.domain.repository;

import com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate.OrderStatus;

public interface OrderStatusRepository {
    OrderStatus findByType(String type);
}
