package com.pulse.shoppingcart;

import com.pulse.shoppingcart.domain.dto.CartDto;
import com.pulse.shoppingcart.domain.model.Cart;
import com.pulse.shoppingcart.domain.model.CartItem;
import com.pulse.shoppingcart.domain.model.Customer;
import com.pulse.shoppingcart.domain.model.Product;
import com.pulse.shoppingcart.repository.CartRepository;
import com.pulse.shoppingcart.repository.CustomerRepository;
import com.pulse.shoppingcart.repository.ProductRepository;
import com.pulse.shoppingcart.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    private CartRepository cartRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartRepository = mock(CartRepository.class);
        customerRepository = mock(CustomerRepository.class);
        productRepository = mock(ProductRepository.class);
        cartService = new CartService(cartRepository, customerRepository, productRepository);
    }

    @Test
    void getAllCarts_ReturnsListOfCartDtos() {
        Cart cart1 = new Cart();
        Cart cart2 = new Cart();
        when(cartRepository.findAll()).thenReturn(List.of(cart1, cart2));

        List<CartDto> result = cartService.getAllCarts();

        assertEquals(2, result.size());
    }

    @Test
    void createCart_WithValidCustomerId_ReturnsCart() {
        Customer customer = new Customer();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Cart savedCart = new Cart(customer);
        when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);

        Cart result = cartService.createCart(1L);

        assertNotNull(result);
        assertEquals(customer, result.getCustomer());
    }

    @Test
    void createCart_WithInvalidCustomerId_ThrowsException() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.createCart(999L));
    }

    @Test
    void addItem_WithValidIds_AddsItemAndSavesCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        Product product = new Product();
        product.setId(2L);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.addItem(1L, 2L, 3);

        assertEquals(1, result.getItems().size());
        CartItem item = result.getItems().get(0);
        assertEquals(product, item.getProduct());
        assertEquals(3, item.getQuantity());
        assertEquals(cart, item.getCart());
    }

    @Test
    void addItem_WithInvalidCartId_ThrowsException() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.addItem(1L, 2L, 1));
    }

    @Test
    void addItem_WithInvalidProductId_ThrowsException() {
        Cart cart = new Cart();
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.addItem(1L, 2L, 1));
    }

    @Test
    void createCustomer_SavesAndReturnsCustomer() {
        Customer customer = new Customer();
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = cartService.createCustomer(customer);

        assertEquals(customer, result);
        verify(customerRepository).save(customer);
    }
}

