package com.pulse.checkout.service;

import com.pulse.checkout.domain.dto.CustomerAddressDto;
import com.pulse.checkout.domain.dto.CustomerDto;
import com.pulse.checkout.repository.CustomerAddressRepository;
import com.pulse.checkout.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository addressRepository;

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerDto::new)
                .toList();
    }

    public List<CustomerAddressDto> getCustomerAddresses(Long customerId) {
        return addressRepository.findByCustomerId(customerId).stream()
                .map(CustomerAddressDto::new)
                .toList();
    }

}
