package com.pulse.shoppingcart.domain.dto;

import com.pulse.shoppingcart.domain.model.OrderItem;

import java.math.BigDecimal;

public record OrderItemDto(
        Long id,
        ProductDto product,
        int quantity,
        BigDecimal discount,
        BigDecimal itemTotal
) {
    public OrderItemDto (OrderItem cartItem) {
        this(cartItem.getId(),
                new ProductDto(cartItem.getProduct()),
                cartItem.getQuantity(),
                cartItem.getDiscount(),
                cartItem.getProductTotal());
    }
}
