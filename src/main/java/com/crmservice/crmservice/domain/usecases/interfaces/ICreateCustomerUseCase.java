package com.crmservice.crmservice.domain.usecases.interfaces;

import com.crmservice.crmservice.domain.usecases.createcustomer.CreateCustomerRequest;
import com.crmservice.crmservice.domain.usecases.createcustomer.CreateCustomerResponse;

public interface ICreateCustomerUseCase {
    CreateCustomerResponse createCustomer(CreateCustomerRequest createCustomerRequest);
}
