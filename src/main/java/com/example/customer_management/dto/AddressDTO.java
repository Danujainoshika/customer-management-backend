package com.example.customer_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private  String line1;
    private  String line2;
    private  String city;
    private  String country;
}
