package com.crmservice.crmservice.domain.usecases.getallcustomers;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.interfaces.ICustomerRepositoryService;
import com.crmservice.crmservice.domain.usecases.interfaces.IGetAllCustomersUseCase;

import java.util.List;


public class GetAllCustomersUseCase implements IGetAllCustomersUseCase {

    private final ICustomerRepositoryService customerRepositoryService;

    public GetAllCustomersUseCase(ICustomerRepositoryService customerRepositoryService) {
        this.customerRepositoryService = customerRepositoryService;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return this.customerRepositoryService.getAllActiveCustomers();
    }

}
