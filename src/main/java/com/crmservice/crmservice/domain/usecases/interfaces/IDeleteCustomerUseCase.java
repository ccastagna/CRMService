package com.crmservice.crmservice.domain.usecases.interfaces;

import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.deletecustomer.DeleteCustomerRequest;

public interface IDeleteCustomerUseCase {
    void deleteCustomer(DeleteCustomerRequest deleteCustomerRequest) throws DomainClientException;
}
