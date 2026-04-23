package com.example.customer_management.config;

import com.example.customer_management.dto.CustomerRequestDTO;
import com.example.customer_management.dto.CustomerResponseDTO;
import com.example.customer_management.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequestDTO dto) {
        Customer c = new Customer();
        c.setName(dto.getName());
        c.setNic(dto.getNic());
        c.setDob(dto.getDob());
        return c;
    }

    public CustomerResponseDTO toDTO(Customer c) {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setName(c.getName());
        dto.setNic(c.getNic());
        return dto;
    }
}