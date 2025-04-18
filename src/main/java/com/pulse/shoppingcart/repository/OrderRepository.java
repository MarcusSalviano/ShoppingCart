package com.pulse.shoppingcart.repository;

import com.pulse.shoppingcart.domain.model.Customer;
import com.pulse.shoppingcart.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    public List<Order> findByCustomerOrderByOrderDateDesc(Customer customer);
}
