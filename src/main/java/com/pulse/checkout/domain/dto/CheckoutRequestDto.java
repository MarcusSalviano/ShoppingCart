package com.pulse.checkout.domain.dto;

import com.pulse.checkout.domain.model.PaymentMethod;
import com.pulse.checkout.domain.model.ShippingMethod;

public record CheckoutRequestDto(
        Long addressId,
        ShippingMethod shippingMethod,
        PaymentMethod paymentMethod
) {
}
