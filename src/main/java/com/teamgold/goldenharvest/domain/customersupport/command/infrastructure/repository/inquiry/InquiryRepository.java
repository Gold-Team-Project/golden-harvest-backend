package com.teamgold.goldenharvest.domain.customersupport.command.infrastructure.repository.inquiry;

import com.teamgold.goldenharvest.domain.customersupport.command.domain.inquiry.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry , String> {
    Optional<Inquiry> findByInquiryIdAndUserId(String inquiryId, String userId);
}
