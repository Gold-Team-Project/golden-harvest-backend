package com.teamgold.goldenharvest.domain.master.command.infrastucture.mater;

import com.teamgold.goldenharvest.domain.master.command.domain.master.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SkuRepository extends JpaRepository<Sku, String> {
    boolean existsBySkuNo(String skuNo);

    @Query("select s from Sku s " +
            "join fetch s.produceMaster " +
            "join fetch s.grade " +
            "join fetch s.variety")
    List<Sku> findAllWithDetails();
}
