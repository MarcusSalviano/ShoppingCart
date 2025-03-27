package com.pulse.checkout.service;

import com.pulse.checkout.domain.dto.CartDto;
import com.pulse.checkout.domain.model.Cart;
import com.pulse.checkout.domain.model.CartItem;
import com.pulse.checkout.domain.model.Customer;
import com.pulse.checkout.domain.model.Product;
import com.pulse.checkout.repository.CartRepository;
import com.pulse.checkout.repository.CustomerRepository;
import com.pulse.checkout.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Cart addItem(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        cart.getItems().add(new CartItem(product, quantity, cart));

        return cartRepository.save(cart);
    }


    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
