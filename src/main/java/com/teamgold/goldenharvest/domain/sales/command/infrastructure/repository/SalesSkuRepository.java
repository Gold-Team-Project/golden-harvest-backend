package com.teamgold.goldenharvest.domain.sales.command.infrastructure.repository;

import com.teamgold.goldenharvest.domain.sales.command.domain.SalesSku;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesSkuRepository extends JpaRepository<SalesSku, String> {
}
