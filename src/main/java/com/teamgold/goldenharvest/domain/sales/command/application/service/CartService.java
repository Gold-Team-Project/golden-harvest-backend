package com.teamgold.goldenharvest.domain.sales.command.application.service;

import com.teamgold.goldenharvest.domain.sales.command.application.dto.AddToCartRequest;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.CartResponse;
import com.teamgold.goldenharvest.domain.sales.command.application.dto.UpdateCartItemRequest;

public interface CartService {
    void addItemToCart(String userEmail, AddToCartRequest request);
    CartResponse getCart(String userEmail);
    void updateItemQuantity(String userEmail, UpdateCartItemRequest request);
    void removeItem(String userEmail, String skuNo);
    String placeOrder(String userEmail);
}
