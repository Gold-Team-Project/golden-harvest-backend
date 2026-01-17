package com.teamgold.goldenharvest.domain.sales.command.domain.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_sales_customer_info") // New table for replicated customer data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {

    @Id
    @Column(name = "customer_email", nullable = false)
    private String email;

    @Column(name = "customer_company", length = 20, nullable = false)
    private String company;

    @Column(length = 20)
    private String businessNumber;

    @Column(name = "customer_name", length = 20)
    private String name;

    @Column(name = "customer_phone", length = 20, nullable = false)
    private String phoneNumber;

    @Column(length = 20)
    private String addressLine1;

    @Column(length = 20)
    private String addressLine2;

    @Column(length = 20)
    private String postalCode;

    @Builder
    public Customer(String email, String company, String businessNumber, String name, String phoneNumber,
                    String addressLine1, String addressLine2, String postalCode) {
        this.email = email;
        this.company = company;
        this.businessNumber = businessNumber;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postalCode = postalCode;
    }
}