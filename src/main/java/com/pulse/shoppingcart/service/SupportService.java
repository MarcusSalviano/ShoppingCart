package com.pulse.shoppingcart.service;

import com.pulse.shoppingcart.domain.model.*;
import com.pulse.shoppingcart.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public void generateRecords() {
        List<Customer> customers = generateCustomers();
        generateCustomerAddresses(customers);
        generateProducts();
        generateCarts(customers);
        generateOrders(customers);
        generateDiscountedCarts(customers);
        generateDiscountedOrders(customers);
    }

    private List<Customer> generateCustomers() {
        Customer joao = new Customer("João Silva", "joao.silva@example.com", "111.111.111-11");
        Customer maria = new Customer("Maria Oliveira", "maria.oliveira@example.com", "222.222.222-22");
        Customer carlos = new Customer("Carlos Souza", "carlos.souza@example.com", "333.333.333-33");

        return customerRepository.saveAll(List.of(joao, maria, carlos));
    }

    private void generateCustomerAddresses(List<Customer> customers) {
        Customer joao = customers.get(0);
        Customer maria = customers.get(1);
        Customer carlos = customers.get(2);

        List<CustomerAddress> addresses = List.of(
                new CustomerAddress(null, "Casa", "Rua das Flores", "123", "Apto 101", "Centro",
                        "Fortaleza", "CE", "60000-000", joao),
                new CustomerAddress(null, "Trabalho", "Avenida Beira Mar", "456", "Sala 302", "Meireles",
                        "Fortaleza", "CE", "60165-000", joao),
                new CustomerAddress(null, "Residencial", "Rua das Palmeiras", "789", "Casa 2", "Jardins",
                        "São Paulo", "SP", "01402-000", maria),
                new CustomerAddress(null, "Apartamento", "Avenida Brasil", "321", "Bloco B, Apto 501", "Copacabana",
                        "Rio de Janeiro", "RJ", "22010-000", carlos)
        );

        customerAddressRepository.saveAll(addresses);
    }

    private void generateProducts() {
        List<Product> products = List.of(
                new Product("Smartphone Galaxy S23", BigDecimal.valueOf(3999.90)),
                new Product("Notebook Dell XPS 15", BigDecimal.valueOf(8999.00)),
                new Product("Fone Bluetooth Sony WH-1000XM5", BigDecimal.valueOf(2299.90)),
                new Product("Smart TV LG OLED 55\"", BigDecimal.valueOf(5499.00)),
                new Product("Console PlayStation 5", BigDecimal.valueOf(4499.90)),
                new Product("Câmera Canon EOS R6", BigDecimal.valueOf(12999.00)),
                new Product("Tablet Samsung Galaxy Tab S8", BigDecimal.valueOf(3799.90)),
                new Product("Monitor Gamer 27\" 144Hz", BigDecimal.valueOf(1899.90)),
                new Product("Teclado Mecânico RGB", BigDecimal.valueOf(499.90)),
                new Product("Mouse Sem Fio Logitech MX", BigDecimal.valueOf(399.90))
        );

        productRepository.saveAll(products);
    }

    private void generateCarts(List<Customer> customers) {
        List<Product> products = productRepository.findAll();

        for (Customer customer : customers) {
            Cart cart = new Cart(customer);
            cart = cartRepository.save(cart);

            List<CartItem> items = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                Product product = products.get(i);
                CartItem item = new CartItem(product, i + 1, null, cart);
                items.add(item);
            }

            cart.setItems(items);
            cartRepository.save(cart);
        }
    }

    private void generateOrders(List<Customer> customers) {
        List<Product> products = productRepository.findAll();
        List<CustomerAddress> addresses = customerAddressRepository.findAll();

        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            CustomerAddress address = addresses.get(i);

            Order order = new Order();
            order.setCustomer(customer);
            order.setShippingAddress(address);
            order.setShippingMethod(ShippingMethod.STANDARD);
            order.setPaymentMethod(PaymentMethod.PIX);
            order.setOrderDate(LocalDateTime.now());

            order = orderRepository.save(order);

            List<OrderItem> items = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (int j = 0; j < 2; j++) {
                Product product = products.get((i + j) % products.size());
                int quantity = j + 1;

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(quantity);

                BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
                total = total.add(subtotal);

                items.add(orderItem);
            }

            order.setItems(items);

            orderRepository.save(order);
        }
    }

    private void generateDiscountedCarts(List<Customer> customers) {
        List<Product> products = productRepository.findAll();

        for (Customer customer : customers) {
            Cart cart = new Cart(customer);
            cart.setDiscount(BigDecimal.valueOf(50)); // R$50 de desconto no carrinho
            cart = cartRepository.save(cart);

            List<CartItem> items = new ArrayList<>();
            for (int i = 2; i < 4; i++) {
                Product product = products.get(i);
                CartItem item = new CartItem(product, i, BigDecimal.valueOf(20), cart); // R$20 de desconto no item
                items.add(item);
            }

            cart.setItems(items);
            cartRepository.save(cart);
        }
    }

    private void generateDiscountedOrders(List<Customer> customers) {
        List<Product> products = productRepository.findAll();
        List<CustomerAddress> addresses = customerAddressRepository.findAll();

        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            CustomerAddress address = addresses.get(i);

            Order order = new Order();
            order.setCustomer(customer);
            order.setShippingAddress(address);
            order.setShippingMethod(ShippingMethod.EXPRESS);
            order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
            order.setOrderDate(LocalDateTime.now());
            order.setDiscount(BigDecimal.valueOf(100)); // R$100 de desconto no pedido

            order = orderRepository.save(order);

            List<OrderItem> items = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (int j = 3; j < 5; j++) {
                Product product = products.get((i + j) % products.size());
                int quantity = j;
                BigDecimal discount = BigDecimal.valueOf(30); // R$30 de desconto no item

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(quantity);
                orderItem.setDiscount(discount);

                BigDecimal subtotal = product.getPrice()
                        .multiply(BigDecimal.valueOf(quantity))
                        .subtract(discount);
                total = total.add(subtotal);

                items.add(orderItem);
            }

            order.setItems(items);
            orderRepository.save(order);
        }
    }

}

