package com.example.customer_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDTO {
    private String name;
    private LocalDate dob;
    private String nic;


    private List<String> mobileNumbers;
    private  List<AddressDTO> addresses;
}
