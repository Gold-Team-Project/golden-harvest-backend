package com.teamgold.goldenharvest.domain.master.command.infrastucture.mater;

import com.teamgold.goldenharvest.domain.master.command.domain.master.Sku;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkuRepository extends JpaRepository<Sku, String> {
    boolean existsBySkuNo(String skuNo);
    
}
