package com.pulse.shoppingcart.controller;

import com.pulse.shoppingcart.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/support")
public class SupportController {

    private final SupportService supportService;

    @PostMapping(value = "/generate-records")
    public ResponseEntity<String> generateRecords() {
        supportService.generateRecords();
        return ResponseEntity.ok("Records for Customer and Product Generated!");
    }
}
