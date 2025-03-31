package com.pulse.shoppingcart.controller;

import com.pulse.shoppingcart.domain.dto.CustomerAddressDto;
import com.pulse.shoppingcart.domain.dto.CustomerDto;
import com.pulse.shoppingcart.domain.model.Customer;
import com.pulse.shoppingcart.service.CustomerService;
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

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Customer> getCustomerAdresses(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.saveCustomer(customer));
    }

}
