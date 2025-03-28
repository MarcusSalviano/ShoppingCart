package com.pulse.checkout.controller;

import com.pulse.checkout.domain.dto.CustomerAddressDto;
import com.pulse.checkout.domain.dto.CustomerDto;
import com.pulse.checkout.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping(value = "/{customerId}", produces = "application/json")
    public ResponseEntity<List<CustomerAddressDto>> getCustomerAdresses(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerAddresses(customerId));
    }
}
