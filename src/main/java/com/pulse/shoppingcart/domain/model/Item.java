package com.pulse.shoppingcart.domain.model;

import com.pulse.shoppingcart.util.PriceUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
@MappedSuperclass
public class Item {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private BigDecimal discount;

    public Item(Product product, int quantity, BigDecimal discount) {
        this.product = product;
        this.quantity = quantity;
        this.discount = discount;
    }

    @Transient
    public BigDecimal getProductTotal() {
        return PriceUtils.getProductTotal(this, discount);
    }
}
