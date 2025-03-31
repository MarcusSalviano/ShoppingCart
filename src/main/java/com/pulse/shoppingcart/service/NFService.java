package com.pulse.shoppingcart.service;


import com.pulse.shoppingcart.domain.NF;
import com.pulse.shoppingcart.domain.model.Order;
import com.pulse.shoppingcart.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class NFService {
    private final OrderRepository orderRepository;


    public NF generateNF(Order order) {
        return new NF(order);
    }

    public NF getNfJson (Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        return new NF(order);
    }
}
