package com.teamgold.goldenharvest.domain.sales.command.application.controller;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.AddToCartRequest;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.CartResponse;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.UpdateCartItemRequest;
import com.teamgold.goldenharvest.domain.sales.command.application.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<Void>> addItemToCart(@RequestHeader("Authorization") String authorizationHeader, @RequestBody AddToCartRequest request) {
        // TODO: Spring Security 적용 후, 인증된 사용자 정보에서 이메일을 가져오도록 수정
        String userEmail = "rrrr@naver.com";

        cartService.addItemToCart(authorizationHeader, userEmail, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        // TODO: Spring Security 적용 후, 인증된 사용자 정보에서 이메일을 가져오도록 수정
        String userEmail = "rrrr@naver.com";

        CartResponse cartResponse = cartService.getCart(userEmail);
        return ResponseEntity.ok(ApiResponse.success(cartResponse));
    }

    @PutMapping("/items")
    public ResponseEntity<ApiResponse<Void>> updateCartItemQuantity(@RequestBody UpdateCartItemRequest request) {
        // TODO: Spring Security 적용 후, 인증된 사용자 정보에서 이메일을 가져오도록 수정
        String userEmail = "rrrr@naver.com";

        cartService.updateItemQuantity(userEmail, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/items/{skuNo}")
    public ResponseEntity<ApiResponse<Void>> removeItemFromCart(@PathVariable String skuNo) {
        // TODO: Spring Security 적용 후, 인증된 사용자 정보에서 이메일을 가져오도록 수정
        String userEmail = "rrrr@naver.com";

        cartService.removeItem(userEmail, skuNo);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<String>> checkout() {
        // TODO: Spring Security 적용 후, 인증된 사용자 정보에서 이메일을 가져오도록 수정
        String userEmail = "rrrr@naver.com";

        String cartId = cartService.placeOrder(userEmail);
        return ResponseEntity.ok(ApiResponse.success(cartId));
    }
}
