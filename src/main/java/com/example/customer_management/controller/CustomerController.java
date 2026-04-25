package com.example.customer_management.controller;

import com.example.customer_management.dto.CustomerRequestDTO;
import com.example.customer_management.dto.CustomerResponseDTO;
import com.example.customer_management.model.Customer;
import com.example.customer_management.service.CustomerService;
import com.example.customer_management.service.CustomerUploadService;
import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {

    private  final CustomerService customerService;
    private final CustomerUploadService customerUploadService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> saveCustomer(@RequestBody CustomerRequestDTO customerRequestDTO){
        CustomerResponseDTO response = customerService.createCustomer(customerRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable long id){
        CustomerResponseDTO response = customerService.getCustomerById(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public Page<CustomerResponseDTO> getCustomers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        if(page < 0 ) page = 0;
        if(size > 50) size = 10;

        return customerService.getCustomers(page, size);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@RequestBody CustomerRequestDTO customerRequestDTO, @PathVariable long id){
        CustomerResponseDTO response = customerService.updateCustomer(id, customerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        customerUploadService.processLargeExcel(file.getInputStream());
        return ResponseEntity.ok("File processing stared in the background. It will take a few minutes");
    }
}
