package com.pulse.checkout;

import com.pulse.checkout.domain.*;
import com.pulse.checkout.repository.CartRepository;
import com.pulse.checkout.repository.CustomerAddressRepository;
import com.pulse.checkout.repository.OrderRepository;
import com.pulse.checkout.service.CheckoutService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock private CartRepository cartRepository;
    @Mock private CustomerAddressRepository addressRepository;
    @Mock private OrderRepository orderRepository;

    @InjectMocks private CheckoutService checkoutService;

    private Cart cart;
    private Customer customer;
    private CustomerAddress address;
    private Product product;

    @BeforeEach
    void setUp() {
        customer = new Customer("João Silva", "joao@example.com");
        customer.setId(1L);

        address = new CustomerAddress(
                "Casa", "Rua A", "123", "Apto 1", "Centro",
                "São Paulo", "SP", "01000-000", customer
        );
        address.setId(1L);

        product = new Product("Smartphone", BigDecimal.valueOf(1000));
        product.setId(1L);

        cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(customer);
        cart.setCheckedOut(false);
        cart.setItems(Collections.singletonList(
                new CartItem(product, 2, cart) // Agora usando o novo construtor
        ));
    }

    @Test
    void checkout_ShouldCreateOrder_WhenValidData() {
        // Arrange
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order order = checkoutService.checkout(
                1L, 1L, ShippingMethod.STANDARD, PaymentMethod.CREDIT_CARD
        );

        // Assert
        assertAll(
                () -> assertNotNull(order),
                () -> assertEquals(customer, order.getCustomer()),
                () -> assertEquals(address, order.getShippingAddress()),
                () -> assertEquals(BigDecimal.valueOf(2000), order.getTotal()),
                () -> assertEquals(1, order.getItems().size()),
                () -> assertTrue(cart.isCheckedOut()),
                () -> verify(cartRepository).save(cart)
        );
    }

    @Test
    void checkout_ShouldThrowException_WhenCartNotFound() {
        when(cartRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                checkoutService.checkout(99L, 1L, ShippingMethod.STANDARD, PaymentMethod.CREDIT_CARD)
        );

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void checkout_ShouldThrowException_WhenAddressNotFound() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(addressRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                checkoutService.checkout(1L, 99L, ShippingMethod.STANDARD, PaymentMethod.CREDIT_CARD)
        );

        assertEquals("Address not found", exception.getMessage());
    }

    @Test
    void checkout_ShouldThrowException_WhenCartAlreadyCheckedOut() {
        cart.setCheckedOut(true);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        Exception exception = assertThrows(IllegalStateException.class, () ->
                checkoutService.checkout(1L, 1L, ShippingMethod.STANDARD, PaymentMethod.CREDIT_CARD)
        );

        assertEquals("Cart already checked out", exception.getMessage());
    }

    @Test
    void checkout_ShouldCalculateCorrectTotal_WithMultipleItems() {
        // Arrange - Adiciona mais um item ao carrinho
        Product notebook = new Product("Notebook", BigDecimal.valueOf(2500));
        cart.getItems().add(new CartItem(notebook, 1, cart));

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order order = checkoutService.checkout(1L, 1L, ShippingMethod.EXPRESS, PaymentMethod.PIX);

        // Assert
        assertEquals(BigDecimal.valueOf(4500), order.getTotal()); // (2x1000) + (1x2500)
    }
}
