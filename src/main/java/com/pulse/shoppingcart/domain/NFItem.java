package com.pulse.shoppingcart.domain;

import com.pulse.shoppingcart.domain.model.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class NFItem {
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public NFItem (OrderItem orderItem) {
        this.productName = orderItem.getProduct().getName();
        this.quantity = orderItem.getQuantity();
        this.subtotal = orderItem.getProductTotal();
        this.unitPrice = getUnityPrice();
    }

    private BigDecimal getUnityPrice() {
        return subtotal.divide(new BigDecimal(quantity))
                .setScale(2, RoundingMode.HALF_UP)
                .stripTrailingZeros();
    }
}
