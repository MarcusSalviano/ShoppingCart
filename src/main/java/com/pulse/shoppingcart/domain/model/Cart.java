package com.pulse.shoppingcart.domain.model;

import com.pulse.shoppingcart.util.PriceUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name="carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private boolean checkedOut;

    private BigDecimal discount;

    public Cart(Customer customer) {
        this.customer = customer;
    }

    @Transient
    public BigDecimal getTotal() {
        return PriceUtils.getTotal(items, discount);
    }

    public Cart() {
        this.customer = new Customer();
    }
}
