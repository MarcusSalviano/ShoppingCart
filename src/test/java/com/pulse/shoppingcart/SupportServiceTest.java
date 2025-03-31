package com.pulse.shoppingcart;

import com.pulse.shoppingcart.domain.model.*;
import com.pulse.shoppingcart.repository.*;
import com.pulse.shoppingcart.service.SupportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SupportServiceTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    private SupportService supportService;

    @BeforeEach
    void setUp() {
        supportService = new SupportService(customerRepository, customerAddressRepository, productRepository, cartRepository, orderRepository);
    }

    @Test
    void testGenerateRecords() {
        supportService.generateRecords();

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(3);

        List<CustomerAddress> addresses = customerAddressRepository.findAll();
        assertThat(addresses).isNotEmpty();

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(10);

        List<Cart> carts = cartRepository.findAll();
        assertThat(carts).isNotEmpty();

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).isNotEmpty();
    }
}