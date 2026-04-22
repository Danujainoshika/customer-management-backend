package com.example.customer_management.repository;

import com.example.customer_management.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country,Long> {

    Optional<Country> findByName(String name);
}
