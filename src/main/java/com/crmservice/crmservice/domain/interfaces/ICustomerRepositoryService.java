package com.crmservice.crmservice.domain.interfaces;

import com.crmservice.crmservice.domain.entities.Customer;

import java.util.Optional;

public interface ICustomerRepositoryService {
    Optional<Customer> getCustomerById(String customerId);

    Optional<Customer> getActiveCustomerById(String customerId);

    Customer saveCustomer(Customer newCustomer);
}
