package com.teamgold.goldenharvest.domain.customersupport.command.infrastructure.repository.inquiry;

import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry , String> {
}
