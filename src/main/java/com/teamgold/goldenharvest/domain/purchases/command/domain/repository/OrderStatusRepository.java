package com.teamgold.goldenharvest.domain.purchases.command.domain.repository;

import com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate.OrderStatus;

import java.util.Optional;

public interface OrderStatusRepository {
    OrderStatus findByType(String type);
}
