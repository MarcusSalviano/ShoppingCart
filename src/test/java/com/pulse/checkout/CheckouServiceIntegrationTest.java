package com.pulse.checkout;

import com.pulse.checkout.domain.model.*;
import com.pulse.checkout.repository.*;
import com.pulse.checkout.service.CheckoutService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(CheckoutService.class)
@ActiveProfiles("test")
class CheckoutServiceIntegrationTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CustomerAddressRepository addressRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CheckoutService checkoutService;

    @PersistenceContext
    private EntityManager entityManager;

    private Customer customer;
    private Product product1;
    private Product product2;
    private CustomerAddress address;
    private Cart cart;

    @BeforeEach
    void setUp() {
        // Clear any existing data
        orderRepository.deleteAll();
        cartRepository.deleteAll();
        addressRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();

        // Create and save test customer
        customer = new Customer("John Doe", "john@example.com");
        customer = customerRepository.save(customer);

        // Create and save test products
        product1 = new Product("Product 1", BigDecimal.valueOf(19.99));
        product2 = new Product("Product 2", BigDecimal.valueOf(9.99));
        product1 = productRepository.save(product1);
        product2 = productRepository.save(product2);

        // Create and save test address
        address = new CustomerAddress();
        address.setAddressName("Home");
        address.setStreet("123 Main St");
        address.setCity("Springfield");
        address.setState("IL");
        address.setZipCode("12345");
        address.setCustomer(customer);
        address = addressRepository.save(address);

        // Create and save test cart with items
        cart = new Cart(customer);
        cart = cartRepository.save(cart);

        CartItem cartItem1 = new CartItem(product1, 2, cart);
        CartItem cartItem2 = new CartItem(product2, 3, cart);

        List<CartItem> items = new ArrayList<>();
        items.add(cartItem1);
        items.add(cartItem2);
        cart.setItems(items);
        cart = cartRepository.save(cart);

        // Flush and clear the persistence context to ensure everything is saved
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void checkout_Successful() {
        // Act
        Order order = checkoutService.checkout(
                cart.getId(),
                address.getId(),
                ShippingMethod.EXPRESS,
                PaymentMethod.CREDIT_CARD
        );

        // Assert
        assertNotNull(order.getId());
        assertEquals(customer.getId(), order.getCustomer().getId());
        assertEquals(address.getId(), order.getShippingAddress().getId());
        assertEquals(ShippingMethod.EXPRESS, order.getShippingMethod());
        assertEquals(PaymentMethod.CREDIT_CARD, order.getPaymentMethod());
        assertNotNull(order.getOrderDate());
        assertFalse(order.getOrderDate().isAfter(LocalDateTime.now()));

        // Verify items and total
        assertEquals(2, order.getItems().size());
        BigDecimal expectedTotal = product1.getPrice().multiply(BigDecimal.valueOf(2))
                .add(product2.getPrice().multiply(BigDecimal.valueOf(3)));
        assertEquals(0, expectedTotal.compareTo(order.getTotal()));

        // Verify cart is marked as checked out
        Cart updatedCart = cartRepository.findById(cart.getId()).orElseThrow();
        assertTrue(updatedCart.isCheckedOut());

        // Verify order is persisted
        assertTrue(orderRepository.existsById(order.getId()));
    }

    @Test
    void checkout_CartNotFound_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () ->
                checkoutService.checkout(999L, address.getId(), ShippingMethod.STANDARD, PaymentMethod.PIX)
        );
    }

    @Test
    void checkout_AddressNotFound_ThrowsException() {
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                checkoutService.checkout(cart.getId(), 999L, ShippingMethod.STANDARD, PaymentMethod.PIX)
        );
    }

    @Test
    void checkout_CartAlreadyCheckedOut_ThrowsException() {
        // Arrange
        cart.setCheckedOut(true);
        cartRepository.save(cart);

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                checkoutService.checkout(cart.getId(), address.getId(), ShippingMethod.STANDARD, PaymentMethod.PIX)
        );
    }

    @Test
    void checkout_EmptyCart_SuccessfulWithZeroTotal() {
        // Arrange
        cart.setItems(new ArrayList<>());
        cart = cartRepository.save(cart);

        // Act
        Order order = checkoutService.checkout(
                cart.getId(),
                address.getId(),
                ShippingMethod.CORREIOS_SEDEX,
                PaymentMethod.BOLETO
        );

        // Assert
        assertEquals(0, order.getItems().size());
        assertEquals(0, BigDecimal.ZERO.compareTo(order.getTotal()));
        assertTrue(cartRepository.findById(cart.getId()).orElseThrow().isCheckedOut());
    }

    @Test
    void checkout_OrderItemsHaveCorrectDetails() {
        // Act
        Order order = checkoutService.checkout(
                cart.getId(),
                address.getId(),
                ShippingMethod.STANDARD,
                PaymentMethod.PIX
        );

        // Assert
        assertEquals(2, order.getItems().size());

        OrderItem item1 = order.getItems().get(0);
        assertEquals(product1.getName(), item1.getProductName());
        assertEquals(2, item1.getQuantity());
        assertEquals(0, product1.getPrice().compareTo(item1.getUnitPrice()));

        OrderItem item2 = order.getItems().get(1);
        assertEquals(product2.getName(), item2.getProductName());
        assertEquals(3, item2.getQuantity());
        assertEquals(0, product2.getPrice().compareTo(item2.getUnitPrice()));
    }

    @Test
    void checkout_VerifyOrderItemsArePersisted() {
        // Act
        Order order = checkoutService.checkout(
                cart.getId(),
                address.getId(),
                ShippingMethod.STANDARD,
                PaymentMethod.PIX
        );

        // Assert
        Order persistedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertEquals(2, persistedOrder.getItems().size());
        assertNotNull(persistedOrder.getItems().get(0).getId());
        assertNotNull(persistedOrder.getItems().get(1).getId());
    }
}