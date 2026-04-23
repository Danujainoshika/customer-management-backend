package com.example.customer_management.service;

import com.example.customer_management.config.CustomerMapper;
import com.example.customer_management.dto.AddressDTO;
import com.example.customer_management.dto.CustomerRequestDTO;
import com.example.customer_management.dto.CustomerResponseDTO;
import com.example.customer_management.exceptionhandler.DuplicateResourceException;
import com.example.customer_management.exceptionhandler.ResourceNotFoundException;
import com.example.customer_management.model.*;
import com.example.customer_management.repository.CityRepository;
import com.example.customer_management.repository.CountryRepository;
import com.example.customer_management.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final CustomerMapper customerMapper;

    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {

        validateCustomer(dto);

        Customer customer = customerMapper.toEntity(dto);

        mapMobiles(dto, customer);
        mapAddresses(dto, customer);

        Customer savedCustomer = customerRepository.save(customer);

        return buildResponse(savedCustomer);
    }

    public CustomerResponseDTO getCustomerById(Long id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Customer not found"));

        return buildResponse(customer);
    }

    public Page<CustomerResponseDTO> getCustomers(int page, int size){
        if(page < 0) page = 0;
        if(size > 50) size = 50;

        Page<Customer> customerPage = customerRepository.findAll(PageRequest.of(page, size));

        return customerPage.map(customer -> buildResponse(customer));
    }

    private void validateCustomer(CustomerRequestDTO dto) {
        if (customerRepository.existsByNic(dto.getNic())) {
            throw new DuplicateResourceException("Customer already exists");
        }
    }

    private void mapMobiles(CustomerRequestDTO dto, Customer customer) {

        if (dto.getMobileNumbers() == null) return;

        List<CustomerMobile> mobiles = dto.getMobileNumbers()
                .stream()
                .map(num -> {
                    CustomerMobile mobile = new CustomerMobile();
                    mobile.setMobileNumber(num);
                    mobile.setCustomer(customer);
                    return mobile;
                })
                .collect(Collectors.toList());

        customer.setMobiles(mobiles);
    }

    private void mapAddresses(CustomerRequestDTO dto, Customer customer) {

        if (dto.getAddresses() == null) return;

        List<Address> addresses = dto.getAddresses()
                .stream()
                .map(a -> {

                    Address address = new Address();
                    address.setLine1(a.getLine1());
                    address.setLine2(a.getLine2());

                    Country country = countryRepository
                            .findByNameIgnoreCase(a.getCountry())
                            .orElseThrow(()->new ResourceNotFoundException("Country not found"));

                    City city = cityRepository
                            .findByNameIgnoreCaseAndCountry(a.getCity(), country)
                            .orElseThrow(()->new ResourceNotFoundException("Country not found"));

                    address.setCountry(country);
                    address.setCity(city);
                    address.setCustomer(customer);

                    return address;
                })
                .collect(Collectors.toList());

        customer.setAddresses(addresses);
    }

    private CustomerResponseDTO buildResponse(Customer customer) {

        CustomerResponseDTO dto = customerMapper.toDTO(customer);

        if (customer.getAddresses() != null) {
            List<AddressDTO> addressDTOs = customer.getAddresses()
                    .stream()
                    .map(a -> {
                        AddressDTO ad = new AddressDTO();
                        ad.setLine1(a.getLine1());
                        ad.setLine2(a.getLine2());
                        ad.setCity(a.getCity().getName());
                        ad.setCountry(a.getCountry().getName());
                        return ad;
                    })
                    .collect(Collectors.toList());

            dto.setAddresses(addressDTOs);
        }

        if (customer.getMobiles() != null) {
            List<String> mobiles = customer.getMobiles()
                    .stream()
                    .map(CustomerMobile::getMobileNumber)
                    .collect(Collectors.toList());

            dto.setMobileNumbers(mobiles);
        }

        return dto;
    }
}