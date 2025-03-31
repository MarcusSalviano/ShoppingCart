package com.pulse.shoppingcart.service;

import com.pulse.shoppingcart.domain.dto.OrderDto;
import com.pulse.shoppingcart.domain.model.Customer;
import com.pulse.shoppingcart.domain.model.Order;
import com.pulse.shoppingcart.repository.CustomerRepository;
import com.pulse.shoppingcart.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderDto::new)
                .toList();
    }

    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return new OrderDto(order);
    }

    public List<OrderDto> getOrderByCustomer(Long customerId) {


        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        List<Order> orders = orderRepository.findByCustomerOrderByOrderDateDesc(customer);

        return orders.stream()
                .map(OrderDto::new)
                .toList();
    }
}
