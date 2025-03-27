package com.pulse.checkout.service;

import com.pulse.checkout.domain.model.Customer;
import com.pulse.checkout.domain.model.CustomerAddress;
import com.pulse.checkout.domain.model.Product;
import com.pulse.checkout.repository.CustomerAddressRepository;
import com.pulse.checkout.repository.CustomerRepository;
import com.pulse.checkout.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final ProductRepository productRepository;

    public void generateRecords() {
        Customer joao = new Customer("João Silva", "joao.silva@example.com");
        Customer maria = new Customer("Maria Oliveira", "maria.oliveira@example.com");
        Customer carlos = new Customer("Carlos Souza", "carlos.souza@example.com");

        // Endereços com todos os campos preenchidos
        List<CustomerAddress> addresses = Arrays.asList(
                // Endereços do João
                new CustomerAddress(null,
                        "Casa",
                        "Rua das Flores", "123", "Apto 101", "Centro",
                        "Fortaleza", "CE", "60000-000", joao),

                new CustomerAddress(null,
                        "Trabalho",
                        "Avenida Beira Mar", "456", "Sala 302", "Meireles",
                        "Fortaleza", "CE", "60165-000", joao),

                // Endereço da Maria
                new CustomerAddress(null,
                        "Residencial",
                        "Rua das Palmeiras", "789", "Casa 2", "Jardins",
                        "São Paulo", "SP", "01402-000", maria),

                // Endereço do Carlos
                new CustomerAddress(null,
                        "Apartamento",
                        "Avenida Brasil", "321", "Bloco B, Apto 501", "Copacabana",
                        "Rio de Janeiro", "RJ", "22010-000", carlos)
        );

        // Persistência (usando cascade ou salvando separadamente)
        customerRepository.saveAll(Arrays.asList(joao, maria, carlos));
        customerAddressRepository.saveAll(addresses);

        List<Product> products = Arrays.asList(
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
}
