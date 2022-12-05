package com.crmservice.crmservice.domain.interfaces;

import com.crmservice.crmservice.domain.entities.Customer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ICustomerRepositoryService {
    Optional<Customer> getCustomerById(String customerId);

    Optional<Customer> getActiveCustomerById(String customerId);

    Customer saveCustomer(Customer newCustomer);

    List<Customer> getAllActiveCustomers();

    String saveCustomerPhoto(MultipartFile photo) throws IOException;
}
