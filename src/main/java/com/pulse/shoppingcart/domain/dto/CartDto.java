package com.pulse.shoppingcart.domain.dto;

import com.pulse.shoppingcart.domain.model.Cart;
import com.pulse.shoppingcart.domain.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

public record CartDto (
    Long id,
    List<CartItem> items,
    CustomerDto customer,
    BigDecimal discount,
    boolean checkedOut
) {
    public CartDto (Cart cart) {
        this(cart.getId(), cart.getItems(), new CustomerDto(cart.getCustomer()), cart.getDiscount(), cart.isCheckedOut());
    }
}
