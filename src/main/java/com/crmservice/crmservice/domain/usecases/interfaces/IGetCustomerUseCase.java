package com.crmservice.crmservice.domain.usecases.interfaces;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.responses.DomainClientException;

public interface IGetCustomerUseCase {
    Customer getCustomer(String customerId) throws DomainClientException;
}
