package com.pulse.shoppingcart.domain.model;

import com.pulse.shoppingcart.util.PriceUtils;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
@MappedSuperclass
public class Item {
    @ManyToOne
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
