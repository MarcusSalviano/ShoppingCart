package com.pulse.shoppingcart.domain.dto;

import com.pulse.shoppingcart.domain.model.CartItem;

import java.math.BigDecimal;

public record CartItemDto (
        Long id,
        ProductDto product,
        int quantity,
        BigDecimal discount,
        BigDecimal itemTotal
) {
    public CartItemDto (CartItem cartItem) {
        this(cartItem.getId(),
                new ProductDto(cartItem.getProduct()),
                cartItem.getQuantity(),
                cartItem.getDiscount(),
                cartItem.getProductTotal());
    }
}
