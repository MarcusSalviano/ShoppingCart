package com.pulse.checkout.controller;

import com.pulse.checkout.domain.CheckoutRequestDto;
import com.pulse.checkout.domain.Order;
import com.pulse.checkout.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping("/{cartId}")
    public ResponseEntity<Order> checkout(@PathVariable Long cartId, @RequestBody CheckoutRequestDto request) {
        Order order = checkoutService.checkout(cartId, request.addressId(), request.shippingMethod(), request.paymentMethod());
        return ResponseEntity.ok(order);
    }
}
