package com.pulse.shoppingcart.domain.dto;

import com.pulse.shoppingcart.domain.model.Customer;

import java.util.List;

public record CustomerDto(
        Long id,
        String name,
        String email,
        List<CustomerAddressDto> customerAdresses
) {
    public CustomerDto(Customer customer) {
        this(customer.getId(), customer.getName(), customer.getEmail(),
                customer.getAddresses().stream()
                .map(CustomerAddressDto::new)
                .toList());
    }
}


