package com.teamgold.goldenharvest.domain.sales.command.application.controller;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.AddToCartRequest;
import com.teamgold.goldenharvest.domain.sales.command.application.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<Void>> addItemToCart(@RequestBody AddToCartRequest request) {
        // TODO: Spring Security 적용 후, 인증된 사용자 정보에서 이메일을 가져오도록 수정
        String userEmail = "testuser@example.com";

        cartService.addItemToCart(userEmail, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
