package com.pulse.shoppingcart.domain.dto;

import com.pulse.shoppingcart.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,
        CustomerDto customer,
        List<OrderItemDto> items,
        BigDecimal discount,
        CustomerAddressDto shippingAddress,
        ShippingMethod shippingMethod,
        PaymentMethod paymentMethod,
        LocalDateTime orderDate,
        BigDecimal orderTotal

) {
    public OrderDto(Order order) {
        this(order.getId(),
                new CustomerDto(order.getCustomer()),
                order.getItems().stream().map(OrderItemDto::new).toList(),
                order.getDiscount(),
                new CustomerAddressDto(order.getShippingAddress()),
                order.getShippingMethod(),
                order.getPaymentMethod(),
                order.getOrderDate(),
                order.getTotal());
    }
}
