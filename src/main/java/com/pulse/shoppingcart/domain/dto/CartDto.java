package com.pulse.shoppingcart.domain.dto;

import com.pulse.shoppingcart.domain.model.Cart;

import java.math.BigDecimal;
import java.util.List;

public record CartDto (
    Long id,
    List<CartItemDto> items,
    CustomerDto customer,
    BigDecimal discount,
    BigDecimal cartTotal,
    boolean checkedOut
) {
    public CartDto (Cart cart) {
        this(cart.getId(),
                cart.getItems().stream().map(CartItemDto::new).toList(),
                new CustomerDto(cart.getCustomer()),
                cart.getDiscount(),
                cart.getTotal(),
                cart.isCheckedOut());
    }
}
