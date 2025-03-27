package com.pulse.checkout.controller;

import com.pulse.checkout.domain.dto.AddItemDto;
import com.pulse.checkout.domain.model.Cart;
import com.pulse.checkout.domain.dto.CartDto;
import com.pulse.checkout.domain.model.Customer;
import com.pulse.checkout.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CartDto>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    @PostMapping(value = "/{customerId}", produces = "application/json")
    public ResponseEntity<Cart> createCart(@PathVariable Long customerId) {
        return ResponseEntity.ok(cartService.createCart(customerId));
    }

    @PostMapping(value = "/{cartId}/items")
    public ResponseEntity<Cart> addItem(@PathVariable Long cartId, @RequestBody AddItemDto request) {
        return ResponseEntity.ok(cartService.addItem(cartId, request.productId(), request.quantity()));
    }

    @PostMapping(value = "/customer", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(cartService.createCustomer(customer));
    }
}
