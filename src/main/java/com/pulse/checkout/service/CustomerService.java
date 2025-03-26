package com.pulse.checkout.service;

import com.pulse.checkout.domain.Customer;
import com.pulse.checkout.domain.CustomerAddress;
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

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<CustomerAddress> getCustomerAddresses(Long customerId) {
        return addressRepository.findByCustomerId(customerId);
    }

}
