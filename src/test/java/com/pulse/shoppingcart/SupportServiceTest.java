package com.pulse.shoppingcart;

import com.pulse.shoppingcart.repository.*;
import com.pulse.shoppingcart.service.SupportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class SupportServiceTest {

    private CustomerRepository customerRepository;
    private CustomerAddressRepository customerAddressRepository;
    private ProductRepository productRepository;
    private SupportService supportService;
    private CartRepository cartRepository;
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerAddressRepository = mock(CustomerAddressRepository.class);
        productRepository = mock(ProductRepository.class);
        supportService = new SupportService(customerRepository, customerAddressRepository, productRepository, cartRepository, orderRepository);
    }

    @Test
    void generateRecords_PersistsCustomersAddressesAndProducts() {
        // Arrange
        when(customerRepository.saveAll(anyList())).thenReturn(null); // ou um valor de retorno adequado
        when(customerAddressRepository.saveAll(anyList())).thenReturn(null);
        when(productRepository.saveAll(anyList())).thenReturn(null);

        // Act
        supportService.generateRecords();

        // Assert
        verify(customerRepository, times(1)).saveAll(anyList());
        verify(customerAddressRepository, times(1)).saveAll(anyList());
        verify(productRepository, times(1)).saveAll(anyList());
    }
}
