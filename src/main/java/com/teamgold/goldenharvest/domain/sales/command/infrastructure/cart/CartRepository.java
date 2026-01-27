package com.teamgold.goldenharvest.domain.sales.command.infrastructure.cart;

import com.teamgold.goldenharvest.domain.sales.command.domain.cart.Cart;
import com.teamgold.goldenharvest.domain.sales.command.domain.cart.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {

    /**
     * 사용자의 이메일과 장바구니 상태로 활성화된 장바구니를 찾습니다.
     * @param userEmail 사용자 이메일
     * @param status 장바구니 상태 (주로 ACTIVE)
     * @return Cart 객체를 담은 Optional
     */
    Optional<Cart> findByUserEmailAndStatus(String userEmail, CartStatus status);
}
