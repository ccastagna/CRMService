package com.crmservice.crmservice.domain.usecases.interfaces;

import com.crmservice.crmservice.domain.entities.Customer;

import java.util.List;

public interface IGetAllCustomersUseCase {
    List<Customer> getAllCustomers();
}
