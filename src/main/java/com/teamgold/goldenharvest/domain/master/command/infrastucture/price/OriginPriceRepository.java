package com.teamgold.goldenharvest.domain.master.command.infrastucture.price;

import com.teamgold.goldenharvest.domain.master.command.domain.price.OriginPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OriginPriceRepository extends JpaRepository<OriginPrice, Long> {
    @Query("select p from OriginPrice p join fetch p.sku")
    List<OriginPrice> findAllWithSku();
}
