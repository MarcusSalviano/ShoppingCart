package com.pulse.shoppingcart;

import com.pulse.shoppingcart.domain.dto.CustomerAddressDto;
import com.pulse.shoppingcart.domain.dto.CustomerDto;
import com.pulse.shoppingcart.domain.model.Customer;
import com.pulse.shoppingcart.domain.model.CustomerAddress;
import com.pulse.shoppingcart.repository.CustomerAddressRepository;
import com.pulse.shoppingcart.repository.CustomerRepository;
import com.pulse.shoppingcart.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private CustomerAddressRepository addressRepository;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        addressRepository = mock(CustomerAddressRepository.class);
        customerService = new CustomerService(customerRepository, addressRepository);
    }

    @Test
    void getAllCustomers_ReturnsListOfCustomerDtos() {
        Customer c1 = new Customer();
        Customer c2 = new Customer();
        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));

        List<CustomerDto> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
    }

    @Test
    void getCustomerAddresses_ReturnsListOfCustomerAddressDtos() {
        CustomerAddress a1 = new CustomerAddress();
        CustomerAddress a2 = new CustomerAddress();
        when(addressRepository.findByCustomerId(1L)).thenReturn(List.of(a1, a2));

        List<CustomerAddressDto> result = customerService.getCustomerAddresses(1L);

        assertEquals(2, result.size());
    }
}

