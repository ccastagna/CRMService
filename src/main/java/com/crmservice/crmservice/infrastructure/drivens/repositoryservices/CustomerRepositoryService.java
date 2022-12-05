package com.crmservice.crmservice.infrastructure.drivens.repositoryservices;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.interfaces.ICustomerRepositoryService;
import com.crmservice.crmservice.infrastructure.drivens.repositories.ICustomerRepository;
import com.crmservice.crmservice.infrastructure.drivens.repositorydtos.CustomerRepositoryDTO;

import java.util.Optional;

public class CustomerRepositoryService implements ICustomerRepositoryService {

    private final ICustomerRepository customerRepository;

    public CustomerRepositoryService(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> getCustomerById(String customerId) {
        return this.customerRepository.findById(customerId)
                .map(CustomerRepositoryDTO::toEntity);
    }

    @Override
    public Optional<Customer> getActiveCustomerById(String customerId) {
        return this.customerRepository.findById(customerId)
                .filter(CustomerRepositoryDTO::isNotDeleted)
                .map(CustomerRepositoryDTO::toEntity);
    }

    @Override
    public Customer saveCustomer(Customer newCustomer) {
        return this.customerRepository.save(CustomerRepositoryDTO.from(newCustomer)).toEntity();
    }
}
