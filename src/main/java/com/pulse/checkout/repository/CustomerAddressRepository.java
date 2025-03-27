package com.pulse.checkout.repository;

import com.pulse.checkout.domain.model.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {
    List<CustomerAddress> findByCustomerId(Long customerId);
}
