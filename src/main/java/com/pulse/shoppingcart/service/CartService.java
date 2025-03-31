package com.pulse.shoppingcart.service;

import com.pulse.shoppingcart.domain.dto.CartDto;
import com.pulse.shoppingcart.domain.model.Cart;
import com.pulse.shoppingcart.domain.model.CartItem;
import com.pulse.shoppingcart.domain.model.Customer;
import com.pulse.shoppingcart.domain.model.Product;
import com.pulse.shoppingcart.repository.CartRepository;
import com.pulse.shoppingcart.repository.CustomerRepository;
import com.pulse.shoppingcart.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public List<CartDto> getAllCarts() {
        return cartRepository.findAll().stream()
                .map(CartDto::new)
                .toList();
    }

    public Cart createCart(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return cartRepository.save(new Cart(customer));
    }

    public Cart addItem(Long cartId, Long productId, Integer quantity, BigDecimal discount) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        if (cart.isCheckedOut()) {
            throw new IllegalStateException("Cannot add items to a checked out cart");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        cart.getItems().add(new CartItem(product, quantity, discount, cart));

        return cartRepository.save(cart);
    }

    public Cart addDiscount(Long cartId, BigDecimal discount) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        if (cart.isCheckedOut()) {
            throw new IllegalStateException("Cannot add items to a checked out cart");
        }

        cart.setDiscount(discount);

        return cartRepository.save(cart);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
