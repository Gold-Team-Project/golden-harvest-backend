package com.teamgold.goldenharvest.domain.purchases.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tb_order_status")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderStatus {

    @Id
    @Column(name = "order_status_type", length = 20)
    private String type;

    @Column(name = "order_status_name", length = 20)
    private String name;

}
