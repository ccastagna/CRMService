package com.crmservice.crmservice.domain.usecases.deletecustomer;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.enums.CustomerState;
import com.crmservice.crmservice.domain.interfaces.ICustomerRepositoryService;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.interfaces.IDeleteCustomerUseCase;

import java.time.Instant;

import static com.crmservice.crmservice.domain.responses.DomainErrorResponse.CUSTOMER_DOES_NOT_EXIST;

public class DeleteCustomerUseCase implements IDeleteCustomerUseCase {

    private final ICustomerRepositoryService customerRepositoryService;

    public DeleteCustomerUseCase(ICustomerRepositoryService customerRepositoryService) {
        this.customerRepositoryService = customerRepositoryService;
    }

    @Override
    public void deleteCustomer(DeleteCustomerRequest deleteCustomerRequest) throws DomainClientException {

        String customerId = deleteCustomerRequest.customerToDelete();

        Customer customerToDelete = this.customerRepositoryService.getActiveCustomerById(customerId)
                .orElseThrow(new DomainClientException(CUSTOMER_DOES_NOT_EXIST));

        Customer deletedCustomer = new Customer(
                customerId,
                customerToDelete.getName(),
                customerToDelete.getSurname(),
                customerToDelete.getDocument(),
                customerToDelete.getCreatedAt(),
                customerToDelete.getCreatedBy(),
                Instant.now(),
                deleteCustomerRequest.currentUser(),
                customerToDelete.getPhotoUrl(),
                CustomerState.DELETED);

        this.customerRepositoryService.saveCustomer(deletedCustomer);
    }
}
