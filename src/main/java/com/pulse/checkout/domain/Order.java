package com.pulse.checkout.domain;

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
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Customer customer;
    @OneToMany(cascade = CascadeType.ALL) private List<OrderItem> items;

    private BigDecimal total;
    private CustomerAddress shippingAddress;
    private ShippingMethod shippingMethod;
    private PaymentMethod paymentMethod;
    private LocalDateTime orderDate;
}
