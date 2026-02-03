package com.teamgold.goldenharvest.domain.sales.query.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesCustomerInfo {
    private String email;
    private String company;
    private String businessNumber;
    private String name;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
}
