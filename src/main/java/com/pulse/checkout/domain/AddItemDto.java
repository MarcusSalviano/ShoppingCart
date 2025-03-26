package com.pulse.checkout.domain;

public record AddItemDto(
        Long productId,
        Integer quantity
) {}
