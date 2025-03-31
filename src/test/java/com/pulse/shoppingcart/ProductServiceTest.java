package com.pulse.shoppingcart;

import com.pulse.shoppingcart.domain.model.Product;
import com.pulse.shoppingcart.repository.ProductRepository;
import com.pulse.shoppingcart.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void getAllProducts_ReturnsListOfProducts() {
        // Arrange
        Product p1 = new Product();
        Product p2 = new Product();

        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(2, result.size(), "The size of the returned list should be 2.");

        verify(productRepository).findAll();
    }
}
