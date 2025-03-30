package com.pulse.shoppingcart.domain.dto;

import com.pulse.shoppingcart.domain.model.PaymentMethod;
import com.pulse.shoppingcart.domain.model.ShippingMethod;

public record CheckoutRequestDto(
        Long addressId,
        ShippingMethod shippingMethod,
        PaymentMethod paymentMethod
) {
}
