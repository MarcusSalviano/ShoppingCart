package com.pulse.shoppingcart.domain.dto;

import com.pulse.shoppingcart.domain.model.Product;

import java.math.BigDecimal;

public record ProductDto(
    Long id,
    String name,
    BigDecimal originalPrice
) {
    public ProductDto(Product product) {
        this(product.getId(), product.getName(), product.getPrice());
    }
}
