package com.pulse.shoppingcart.controller;

import com.pulse.shoppingcart.domain.NF;
import com.pulse.shoppingcart.domain.dto.OrderDto;
import com.pulse.shoppingcart.domain.dto.ReportResponse;
import com.pulse.shoppingcart.service.NFService;
import com.pulse.shoppingcart.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final NFService nfService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long cartId) {
        return ResponseEntity.ok(orderService.getOrderById(cartId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getOrderByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrderByCustomer(customerId));
    }

    @GetMapping(value = "/nf-json/{orderId}", produces = "application/json")
    public ResponseEntity<NF> getNfJson(@PathVariable Long orderId) {
        return ResponseEntity.ok(nfService.getNfJson(orderId));
    }
}
