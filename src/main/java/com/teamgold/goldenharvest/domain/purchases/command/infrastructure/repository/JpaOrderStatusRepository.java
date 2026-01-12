package com.teamgold.goldenharvest.domain.purchases.command.infrastructure.repository;

import com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate.OrderStatus;
import com.teamgold.goldenharvest.domain.purchases.command.domain.repository.OrderStatusRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderStatusRepository extends OrderStatusRepository , JpaRepository<OrderStatus,String> {
}
