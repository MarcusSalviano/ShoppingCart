package com.pulse.shoppingcart.controller;

import com.pulse.shoppingcart.domain.model.Product;
import com.pulse.shoppingcart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Product> getCustomerAdresses(@RequestBody Product product) {
        return ResponseEntity.ok(productService.saveProduct(product));
    }
}
