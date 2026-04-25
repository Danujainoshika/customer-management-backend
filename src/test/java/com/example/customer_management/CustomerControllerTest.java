package com.example.customer_management;

import com.example.customer_management.controller.CustomerController;
import com.example.customer_management.dto.CustomerRequestDTO;
import com.example.customer_management.dto.CustomerResponseDTO;
import com.example.customer_management.service.CustomerService;
import com.example.customer_management.service.CustomerUploadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomerUploadService customerUploadService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create customer")
    void testSaveCustomer_Success() throws Exception {
        CustomerRequestDTO request = new CustomerRequestDTO();
        request.setName("Saman");
        request.setNic("200133002699");

        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(1L);
        response.setName("Saman");


        when(customerService.createCustomer(any(CustomerRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.name").value("Saman"));


    }

    @Test
    @DisplayName("get Customer By Id")
    void testgetCustomerById_Success() throws Exception {
        Long id = 1L;
        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(1L);
        response.setName("Saman");

        when(customerService.getCustomerById(id)).thenReturn(response);

        mockMvc.perform(get("/api/customers/{id}", id))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Saman"));


    }

    @Test
    @DisplayName("upload excel")
    void testUploadFile_Success() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "test data".getBytes()
        );

        doNothing().when(customerUploadService).processLargeExcel(any(InputStream.class));

        mockMvc.perform(multipart("/api/customers/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("File processing stared in the background. It will take a few minutes"));

    }

    @Test
    @DisplayName("update customer")
    void testUpdateCustomer_Success() throws Exception {
        long id = 1L;
        CustomerRequestDTO request = new CustomerRequestDTO();
        request.setName("updated Name");

        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setName("updated Name");

        when(customerService.updateCustomer(eq(id),any(CustomerRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/customers/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("updated Name"));


    }
}
