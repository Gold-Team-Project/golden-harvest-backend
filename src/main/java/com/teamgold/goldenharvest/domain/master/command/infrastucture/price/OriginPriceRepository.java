package com.teamgold.goldenharvest.domain.master.command.infrastucture.price;

import com.teamgold.goldenharvest.domain.master.command.domain.price.OriginPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginPriceRepository extends JpaRepository<OriginPrice, Long> {
}
