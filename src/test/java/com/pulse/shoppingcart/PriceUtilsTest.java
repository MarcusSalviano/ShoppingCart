package com.pulse.shoppingcart;

import com.pulse.shoppingcart.domain.model.Item;
import com.pulse.shoppingcart.domain.model.Product;
import com.pulse.shoppingcart.util.PriceUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PriceUtilsTest {

    @Test
    void getTotal_EmptyList_ReturnsZero() {
        BigDecimal result = PriceUtils.getTotal(Collections.emptyList(), null);
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    void getTotal_NullList_ReturnsZero() {
        BigDecimal result = PriceUtils.getTotal(null, null);
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @ParameterizedTest
    @MethodSource("provideItemsForTotalCalculation")
    void getTotal_WithItems_CalculatesCorrectly(int quantity, BigDecimal price, BigDecimal discount, BigDecimal expected) {
        // Arrange
        Item item = mock(Item.class);
        Product product = mock(Product.class);

        when(item.getProduct()).thenReturn(product);
        when(item.getQuantity()).thenReturn(quantity);
        when(item.getDiscount()).thenReturn(discount);
        when(product.getPrice()).thenReturn(price);

        // Act
        BigDecimal result = PriceUtils.getTotal(List.of(item), null);

        // Assert
        assertEquals(0, expected.compareTo(result),
                String.format("Expected %s but got %s", expected, result));
    }

    @ParameterizedTest
    @MethodSource("provideItemsForProductTotal")
    void getProductTotal_CalculatesCorrectly(int quantity, BigDecimal price, BigDecimal discount, BigDecimal expected) {
        // Arrange
        Item item = mock(Item.class);
        Product product = mock(Product.class);

        when(item.getProduct()).thenReturn(product);
        when(item.getQuantity()).thenReturn(quantity);
        when(item.getDiscount()).thenReturn(discount);
        when(product.getPrice()).thenReturn(price);

        // Act
        BigDecimal result = PriceUtils.getProductTotal(item, discount);

        // Assert
        assertEquals(0, expected.compareTo(result));
    }

    @Test
    void getTotalWithDiscount_NullDiscount_ReturnsOriginalValue() {
        BigDecimal value = new BigDecimal("100.00");
        BigDecimal result = PriceUtils.getTotalWithDiscount(value, null);
        assertEquals(0, value.compareTo(result));
    }

    @ParameterizedTest
    @MethodSource("provideDiscountValues")
    void getTotalWithDiscount_AppliesCorrectDiscount(BigDecimal value, BigDecimal discount, BigDecimal expected) {
        BigDecimal result = PriceUtils.getTotalWithDiscount(value, discount);
        assertEquals(0, expected.compareTo(result));
    }

    private static Stream<Arguments> provideItemsForTotalCalculation() {
        return Stream.of(
                Arguments.of(2, new BigDecimal("10.00"), null, new BigDecimal("20.00")),
                Arguments.of(3, new BigDecimal("5.50"), null, new BigDecimal("16.50")),
                Arguments.of(1, new BigDecimal("20.00"), new BigDecimal("5.00"), new BigDecimal("19.00")),
                Arguments.of(5, new BigDecimal("2.99"), new BigDecimal("10.00"), new BigDecimal("13.46"))
        );
    }

    private static Stream<Arguments> provideItemsForProductTotal() {
        return Stream.of(
                Arguments.of(1, new BigDecimal("10.00"), null, new BigDecimal("10.00")),
                Arguments.of(2, new BigDecimal("15.00"), null, new BigDecimal("30.00")),
                Arguments.of(3, new BigDecimal("20.00"), new BigDecimal("5.00"), new BigDecimal("57.00")),
                Arguments.of(1, new BigDecimal("100.00"), new BigDecimal("10.00"), new BigDecimal("90.00"))
        );
    }

    private static Stream<Arguments> provideDiscountValues() {
        return Stream.of(
                Arguments.of(new BigDecimal("100.00"), new BigDecimal("10.00"), new BigDecimal("90.00")),
                Arguments.of(new BigDecimal("50.00"), new BigDecimal("20.00"), new BigDecimal("40.00")),
                Arguments.of(new BigDecimal("75.50"), new BigDecimal("15.00"), new BigDecimal("64.18")),
                Arguments.of(new BigDecimal("123.45"), new BigDecimal("7.50"), new BigDecimal("114.19"))
        );
    }
}
