package com.pulse.checkout.domain;

public record CheckoutRequestDto(
        Long addressId,
        ShippingMethod shippingMethod,
        PaymentMethod paymentMethod
) {
}
