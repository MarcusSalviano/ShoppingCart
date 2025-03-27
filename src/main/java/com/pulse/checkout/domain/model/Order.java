package com.pulse.checkout.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items;

    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private CustomerAddress shippingAddress;

    private ShippingMethod shippingMethod;
    private PaymentMethod paymentMethod;
    private LocalDateTime orderDate;
}
