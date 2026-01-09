package com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_order_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderStatus {

    @Id
    @Column(name = "order_status_id", length = 20, nullable = false)
    private String id;

    @Column(name = "order_status_name", length = 20)
    private String name;

    @Column(name = "order_status_type", length = 20)
    private String type;

}
