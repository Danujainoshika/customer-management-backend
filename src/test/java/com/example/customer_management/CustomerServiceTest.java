package com.example.customer_management;

import com.example.customer_management.config.CustomerMapper;
import com.example.customer_management.dto.AddressDTO;
import com.example.customer_management.dto.CustomerRequestDTO;
import com.example.customer_management.dto.CustomerResponseDTO;
import com.example.customer_management.model.City;
import com.example.customer_management.model.Country;
import com.example.customer_management.model.Customer;
import com.example.customer_management.repository.CityRepository;
import com.example.customer_management.repository.CountryRepository;
import com.example.customer_management.repository.CustomerRepository;
import com.example.customer_management.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CustomerMapper customerMapper;
    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("Create customer successfully")
     void testCreateCustomer_Success(){
        CustomerRequestDTO request = new CustomerRequestDTO();
        request.setName("Saman");
        request.setNic("200133002699");

        Customer customerEntity = new Customer();
        customerEntity.setName("Saman");
        customerEntity.setNic("200133002699");

        CustomerResponseDTO expectedResponse =  new CustomerResponseDTO();
        expectedResponse.setName("Saman");

        when(customerRepository.existsByNic(request.getNic())).thenReturn(false);
        when(customerMapper.toEntity(request)).thenReturn(customerEntity);
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
        when(customerMapper.toDTO(customerEntity)).thenReturn(expectedResponse);

        CustomerResponseDTO actualResponse = customerService.createCustomer(request);

        assertNotNull(actualResponse);
        assertEquals("Saman",actualResponse.getName());

        verify(customerRepository,times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Get Customer By Id")
    void testGetCustomerById_Success(){
        Long id = 1L;

        Customer customerEntity = new Customer();
        customerEntity.setId(id);
        customerEntity.setName("Saman");
        customerEntity.setNic("200133002699");

        CustomerResponseDTO expectedResponse =  new CustomerResponseDTO();
        expectedResponse.setId(id);
        expectedResponse.setName("Saman");

        when(customerRepository.findById(id)).thenReturn(Optional.of(customerEntity));
        when(customerMapper.toDTO(customerEntity)).thenReturn(expectedResponse);

        CustomerResponseDTO actualResponse = customerService.getCustomerById(id);

        assertNotNull(actualResponse);
        assertEquals("Saman",actualResponse.getName());

        verify(customerRepository,times(1)).findById(id);


    }

    @Test
    @DisplayName("Get Customer list as a pagination")
    void testGetCustomers_Pagination_Success(){
        int page = 0;
        int size = 10;

        Customer customerEntity = new Customer();
        customerEntity.setName("Saman");

        List<Customer> customerList = Arrays.asList(customerEntity);
        Page<Customer> customerPage = new PageImpl(customerList);

        CustomerResponseDTO expectedResponse =  new CustomerResponseDTO();
        expectedResponse.setName("Saman");

        when(customerRepository.findAll(any(PageRequest.class))).thenReturn(customerPage);
        when(customerMapper.toDTO(any(Customer.class))).thenReturn(expectedResponse);

        Page<CustomerResponseDTO> actualResponse = customerService.getCustomers(page, size);

        assertNotNull(actualResponse);
        assertEquals(1,actualResponse.getContent().size());
        assertEquals("Saman",actualResponse.getContent().get(0).getName());

        verify(customerRepository).findAll(PageRequest.of(page,size));

    }

    @Test
    @DisplayName("Update customer")
    void testUpdateCustomer_Success(){
        Long id = 1L;
        Customer customerEntity = new Customer();
        customerEntity.setId(id);
        customerEntity.setName("Old Name");
        customerEntity.setNic("200133002699");
        customerEntity.setMobiles(new ArrayList<>());
        customerEntity.setAddresses(new ArrayList<>());

        CustomerRequestDTO updateDto =  new CustomerRequestDTO();
        updateDto.setName("New Name");
        updateDto.setNic("200133002699");

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCountry("Sri Lanka");
        addressDTO.setCity("Colombo");

        updateDto.setAddresses(Arrays.asList(addressDTO));

        Country mockCountry = new Country();
        mockCountry.setName("Sri Lanka");
        City mockCity = new City();
        mockCity.setName("Colombo");



        CustomerResponseDTO expectedResponse =  new CustomerResponseDTO();
        expectedResponse.setName("New Name");

        when(customerRepository.findById(id)).thenReturn(Optional.of(customerEntity));
        when(countryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(mockCountry));
        when(cityRepository.findByNameIgnoreCaseAndCountry(anyString(), any())).thenReturn(Optional.of(mockCity));
        when(customerRepository.save(any(Customer.class))).thenReturn(customerEntity);
        when(customerMapper.toDTO(customerEntity)).thenReturn(expectedResponse);

        CustomerResponseDTO actualResponse = customerService.updateCustomer(id,updateDto);

        assertNotNull(actualResponse);
        assertEquals("New Name",actualResponse.getName());

        verify(customerRepository,times(1)).findById(id);
        verify(countryRepository,times(1)).findByNameIgnoreCase(anyString());
        verify(customerRepository,times(1)).save(any(Customer.class));








    }

}
