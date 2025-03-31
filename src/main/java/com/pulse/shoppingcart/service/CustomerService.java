package com.pulse.shoppingcart.service;

import com.pulse.shoppingcart.domain.dto.CustomerAddressDto;
import com.pulse.shoppingcart.domain.dto.CustomerDto;
import com.pulse.shoppingcart.domain.model.Customer;
import com.pulse.shoppingcart.domain.model.CustomerAddress;
import com.pulse.shoppingcart.repository.CustomerAddressRepository;
import com.pulse.shoppingcart.repository.CustomerRepository;
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

    public Customer saveCustomer(Customer customer) {
        for (CustomerAddress address : customer.getAddresses()) {
            address.setCustomer(customer);
        }

        return customerRepository.save(customer);
    }
}
