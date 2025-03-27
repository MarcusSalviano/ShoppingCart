package com.pulse.checkout.domain.dto;

public record AddItemDto(
        Long productId,
        Integer quantity
) {}
