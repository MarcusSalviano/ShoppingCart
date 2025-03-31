package com.pulse.shoppingcart.service;

import com.pulse.shoppingcart.domain.NF;
import com.pulse.shoppingcart.domain.model.*;
import com.pulse.shoppingcart.repository.CartRepository;
import com.pulse.shoppingcart.repository.CustomerAddressRepository;
import com.pulse.shoppingcart.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CheckoutService {
    private final CartRepository cartRepository;
    private final CustomerAddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final NFService nfService;

    public Order checkout(Long cartId, Long addressId, ShippingMethod shippingMethod, PaymentMethod paymentMethod) {
        Cart cart = cartRepository.findById(cartId).
                orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        if (cart.isCheckedOut()) throw new IllegalStateException("Cart already checked out");

        CustomerAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        Order order = new Order();
        order.setCustomer(cart.getCustomer());
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(address);
        order.setShippingMethod(shippingMethod);
        order.setPaymentMethod(paymentMethod);
        order.setDiscount(cart.getDiscount());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setDiscount(item.getDiscount());

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        cart.setCheckedOut(true);

        cartRepository.save(cart);
        order = orderRepository.save(order);

        NF nf = nfService.generateNF(order);
        log.info(nf);

        return order;
    }
}
