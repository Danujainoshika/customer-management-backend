package com.example.customer_management.repository;

import com.example.customer_management.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {

    Optional<City> findByName(String name);
}
