package com.pulse.shoppingcart.domain;

import com.pulse.shoppingcart.domain.model.Order;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class NF {

    private Long id;
    private Long orderId;
    private String customerName;
    private String customerDocument;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private String paymentMethod;
    private String shippingMethod;
    private BigDecimal totalValue;
    private List<NFItem> items;

    public NF (Order order) {
        this.id = order.getId();
        this.orderId = order.getId();
        this.customerName = order.getCustomer().getName();
        this.customerDocument = order.getCustomer().getCpf();
        this.orderDate = order.getOrderDate();
        this.shippingAddress = order.getShippingAddress().getStreet() +
                                "," + order.getShippingAddress().getStreet() +
                                " - " + order.getShippingAddress().getComplement();
        this.paymentMethod = order.getPaymentMethod().toString();
        this.shippingMethod = order.getShippingMethod().toString();
        this.totalValue = order.getTotal();
        this.items = order.getItems().stream().map(NFItem::new).toList();
    }
}

