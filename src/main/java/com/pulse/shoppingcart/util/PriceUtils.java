package com.pulse.shoppingcart.util;

import com.pulse.shoppingcart.domain.model.Item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PriceUtils {

    private PriceUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static BigDecimal getTotal(List<? extends Item> items, BigDecimal discount) {
        BigDecimal total = BigDecimal.ZERO;

        if (items != null) {
            for (Item item : items) {
                BigDecimal itemTotal = getProductTotal(item, item.getDiscount());
                total = total.add(itemTotal);
            }
        }

        return getTotalWithDiscount(total, discount);
    }

    public static BigDecimal getProductTotal(Item item, BigDecimal discount) {
        BigDecimal total = BigDecimal.ZERO;

        BigDecimal itemTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        total = total.add(itemTotal);

        return getTotalWithDiscount(total, discount).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal getTotalWithDiscount(BigDecimal total, BigDecimal discount) {
        if (discount != null) {
            total = total.multiply(BigDecimal.valueOf(1).subtract(discount.divide(BigDecimal.valueOf(100))));
        }
        return total
                .setScale(2, RoundingMode.HALF_UP)
                .stripTrailingZeros();
    }
}
