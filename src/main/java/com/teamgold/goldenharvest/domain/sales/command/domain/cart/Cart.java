package com.teamgold.goldenharvest.domain.sales.command.domain.cart;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_cart")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @Column(name = "cart_id", length = 20, nullable = false)
    private String cartId; // 고유 번호

    @Column(name = "user_email", length = 20, nullable = false)
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 1)
    private CartStatus status;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter // CartServiceImpl에서 cartItems를 설정하기 위해 필요
    private List<CartItem> cartItems;
}

