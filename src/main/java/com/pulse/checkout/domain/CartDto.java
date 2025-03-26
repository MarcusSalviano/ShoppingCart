package com.pulse.checkout.domain;

import java.util.List;

public record CartDto (
    Long id,
    List<CartItem> items,
    Customer customer,
    boolean checkedOut
) {
    public CartDto (Cart cart) {
        this(cart.getId(), cart.getItems(), cart.getCustomer(), cart.isCheckedOut());
    }
}
