package com.example.customer_management.repository;

import com.example.customer_management.model.Country;
import com.example.customer_management.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByNic(String nic);

    boolean existsByNic(String nic);

    Country findByNameIgnoreCase(String name);
}
