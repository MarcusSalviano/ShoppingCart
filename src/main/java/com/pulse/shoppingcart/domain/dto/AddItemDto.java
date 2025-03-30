package com.pulse.shoppingcart.domain.dto;

public record AddItemDto(
        Long productId,
        Integer quantity
) {}
