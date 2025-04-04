package com.pulse.shoppingcart.domain.dto;

import java.math.BigDecimal;

public record AddItemDto(
        Long productId,
        Integer quantity,
        BigDecimal discount
) {}
