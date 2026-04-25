package com.example.customer_management.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.example.customer_management.dto.CustomerExcelDTO;
import com.example.customer_management.model.Address;
import com.example.customer_management.model.City;
import com.example.customer_management.model.Customer;
import com.example.customer_management.repository.CityRepository;
import com.example.customer_management.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;

@Service
public class CustomerUploadService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Async
    public  void processLargeExcel(InputStream inputStream){
        Map<String, City> cityMap = cityRepository.findAll()
                .stream()
                .collect(Collectors.toMap(City::getName,city->city));

        EasyExcel.read(inputStream, CustomerExcelDTO.class, new ReadListener<CustomerExcelDTO>() {

            private List<Customer> batchList = new ArrayList<>();

            @Override
            public void invoke(CustomerExcelDTO data, AnalysisContext context) {
                Customer customer = new Customer();
                customer.setName(data.getName());
                customer.setNic(data.getNic());
                customer.setDob(LocalDate.parse(data.getDob()));


                 City city = cityMap.get(data.getCityName());

                if(city != null){
                    Address address = new Address();

                    address.setCity(city);

                    if(city.getCountry() != null){
                        address.setCountry(city.getCountry());
                    }
                    customer.addAddress(address);
                }

                batchList.add(customer);


                if (batchList.size() >= 1000) {
                    saveToDatabase();
                }
            }

            private void saveToDatabase() {
                customerRepository.saveAll(batchList);
                batchList.clear();
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                saveToDatabase();
            }
        }).sheet().doRead();
    }

}
