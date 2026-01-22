package com.teamgold.goldenharvest.domain.sales.command.application.service;

import com.teamgold.goldenharvest.domain.sales.command.application.dto.AddToCartRequest;

public interface CartService {
    void addItemToCart(String userEmail, AddToCartRequest request);
}
