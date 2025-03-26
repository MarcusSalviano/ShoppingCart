package com.pulse.checkout.controller;

import com.pulse.checkout.domain.Product;
import com.pulse.checkout.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/support")
public class SupportController {

    private final SupportService supportService;

    @GetMapping(value = "/products", produces = "application/json")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(supportService.getAllProducts());
    }

    @PostMapping(value = "/generate-records")
    public ResponseEntity<String> generateRecords() {
        supportService.generateRecords();
        return ResponseEntity.ok("Records for Customer and Product Generated!");
    }
}
