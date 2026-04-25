package com.example.customer_management.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerExcelDTO {
    @ExcelProperty("Name")
    private String name;

    @ExcelProperty("NIC")
    private String nic;

    @ExcelProperty("Date of Birth")
    private String dob;

    @ExcelProperty("City")
    private String cityName;
}
