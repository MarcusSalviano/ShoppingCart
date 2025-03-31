package com.pulse.shoppingcart;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.pulse.shoppingcart.domain.model.*;
import com.pulse.shoppingcart.repository.CartRepository;
import com.pulse.shoppingcart.repository.CustomerAddressRepository;
import com.pulse.shoppingcart.repository.OrderRepository;
import com.pulse.shoppingcart.service.CheckoutService;
import com.pulse.shoppingcart.util.PriceUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CustomerAddressRepository addressRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CheckoutService checkoutService;

    private Cart cart;
    private Customer customer;
    private CustomerAddress address;
    private Product product1, product2, product3;
    private CartItem cartItem1, cartItem2, cartItemWithDiscount;

    @BeforeEach
    void setUp() {
        customer = new Customer("John Doe", "john@example.com");
        customer.setId(1L);

        address = new CustomerAddress();
        address.setId(1L);
        address.setCustomer(customer);
        address.setStreet("Main St");
        address.setCity("Springfield");
        address.setState("IL");
        address.setZipCode("12345");

        product1 = new Product("Product 1", BigDecimal.valueOf(10.99));
        product1.setId(1L);
        product2 = new Product("Product 2", BigDecimal.valueOf(5.50));
        product2.setId(2L);
        product3 = new Product("Product 3", BigDecimal.valueOf(20.00));
        product3.setId(3L);

        cart = new Cart(customer);
        cart.setId(1L);
        cart.setCheckedOut(false);
        cart.setDiscount(BigDecimal.valueOf(10)); // 10% discount for the whole cart

        cartItem1 = new CartItem(product1, 2, null, cart); // no item discount
        cartItem2 = new CartItem(product2, 3, null, cart); // no item discount
        cartItemWithDiscount = new CartItem(product3, 1, BigDecimal.valueOf(5), cart); // 5% item discount

        List<CartItem> items = new ArrayList<>();
        items.add(cartItem1);
        items.add(cartItem2);
        items.add(cartItemWithDiscount);
        cart.setItems(items);
    }

    @Test
    void checkout_Successful() {
        // Arrange
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = checkoutService.checkout(1L, 1L, ShippingMethod.STANDARD, PaymentMethod.CREDIT_CARD);

        // Assert
        assertNotNull(result);
        assertEquals(customer, result.getCustomer());
        assertEquals(address, result.getShippingAddress());
        assertEquals(ShippingMethod.STANDARD, result.getShippingMethod());
        assertEquals(PaymentMethod.CREDIT_CARD, result.getPaymentMethod());
        assertEquals(cart.getDiscount(), result.getDiscount());
        assertNotNull(result.getOrderDate());
        assertFalse(result.getOrderDate().isAfter(LocalDateTime.now()));

        assertEquals(3, result.getItems().size());

        BigDecimal item1Total = PriceUtils.getProductTotal(cartItem1, null);
        BigDecimal item2Total = PriceUtils.getProductTotal(cartItem2, null);
        BigDecimal item3Total = PriceUtils.getProductTotal(cartItemWithDiscount, cartItemWithDiscount.getDiscount());
        BigDecimal subtotal = item1Total.add(item2Total).add(item3Total);
        BigDecimal expectedTotal = PriceUtils.getTotalWithDiscount(subtotal, cart.getDiscount());

        assertEquals(0, expectedTotal.compareTo(result.getTotal()));

        assertTrue(cart.isCheckedOut());

        verify(cartRepository).findById(1L);
        verify(addressRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }


    @Test
    void checkout_CartNotFound_ThrowsException() {
        // Arrange
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                checkoutService.checkout(1L, 1L, ShippingMethod.STANDARD, PaymentMethod.CREDIT_CARD)
        );

        verify(cartRepository).findById(1L);
        verifyNoInteractions(addressRepository, orderRepository);
    }

    @Test
    void checkout_AddressNotFound_ThrowsException() {
        // Arrange
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                checkoutService.checkout(1L, 1L, ShippingMethod.STANDARD, PaymentMethod.CREDIT_CARD)
        );

        verify(cartRepository).findById(1L);
        verify(addressRepository).findById(1L);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void checkout_CartAlreadyCheckedOut_ThrowsException() {
        // Arrange
        cart.setCheckedOut(true);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                checkoutService.checkout(1L, 1L, ShippingMethod.STANDARD, PaymentMethod.CREDIT_CARD)
        );

        verify(cartRepository).findById(1L);
        verifyNoInteractions(addressRepository, orderRepository);
    }

    @Test
    void checkout_EmptyCart_SuccessfulWithZeroTotal() {
        // Arrange
        cart.setItems(new ArrayList<>());
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = checkoutService.checkout(1L, 1L, ShippingMethod.EXPRESS, PaymentMethod.PIX);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getItems().size());
        assertEquals(0, result.getTotal().compareTo(BigDecimal.ZERO));
        assertTrue(cart.isCheckedOut());
    }

    @Test
    void checkout_VerifyOrderItemDetails() {
        // Arrange
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = checkoutService.checkout(1L, 1L, ShippingMethod.CORREIOS_PAC, PaymentMethod.BOLETO);

        // Assert
        assertEquals(3, result.getItems().size());

        result.getItems().forEach(orderItem -> {
            assertNotNull(orderItem.getProduct());
            assertTrue(orderItem.getQuantity() > 0);
            assertEquals(orderItem.getDiscount(),
                    cart.getItems().stream()
                            .filter(ci -> ci.getProduct().getId().equals(orderItem.getProduct().getId()))
                            .findFirst()
                            .map(CartItem::getDiscount)
                            .orElse(null));
        });
    }
}
