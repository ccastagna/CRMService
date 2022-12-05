package com.crmservice.crmservice.domain.usecases.getcustomer;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.interfaces.ICustomerRepositoryService;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.responses.DomainErrorResponse;
import com.crmservice.crmservice.domain.usecases.interfaces.IGetCustomerUseCase;

public class GetCustomerUseCase implements IGetCustomerUseCase {
    private final ICustomerRepositoryService customerRepositoryService;

    public GetCustomerUseCase(ICustomerRepositoryService customerRepositoryService) {
        this.customerRepositoryService = customerRepositoryService;
    }
    @Override
    public Customer getCustomer(String customerId) throws DomainClientException {
        return this.customerRepositoryService.getActiveCustomerById(customerId)
                .orElseThrow(new DomainClientException(DomainErrorResponse.CUSTOMER_DOES_NOT_EXIST));
    }
}
