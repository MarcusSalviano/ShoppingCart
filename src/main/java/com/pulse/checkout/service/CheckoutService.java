package com.pulse.checkout.service;

import com.pulse.checkout.domain.*;
import com.pulse.checkout.repository.CartRepository;
import com.pulse.checkout.repository.CustomerAddressRepository;
import com.pulse.checkout.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final CustomerAddressRepository addressRepository;
    private final OrderRepository orderRepository;

    public Order checkout(Long cartId, Long addressId, ShippingMethod shippingMethod, PaymentMethod paymentMethod) {
        Cart cart = cartRepository.findById(cartId).orElseThrow();
        if (cart.isCheckedOut()) throw new IllegalStateException("Cart already checked out");

        CustomerAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        Order order = new Order();
        order.setCustomer(cart.getCustomer());
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(address);
        order.setShippingMethod(shippingMethod);
        order.setPaymentMethod(paymentMethod);

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(item.getProduct().getName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(item.getProduct().getPrice());

            total = total.add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotal(total);
        cart.setCheckedOut(true);

        return orderRepository.save(order);
    }
}
