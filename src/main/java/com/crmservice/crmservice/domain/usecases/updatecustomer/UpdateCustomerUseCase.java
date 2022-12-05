package com.crmservice.crmservice.domain.usecases.updatecustomer;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.enums.CustomerState;
import com.crmservice.crmservice.domain.interfaces.ICustomerRepositoryService;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.interfaces.IUpdateCustomerUseCase;

import java.time.Instant;
import java.util.Optional;

import static com.crmservice.crmservice.domain.responses.DomainErrorResponse.CUSTOMER_DOES_NOT_EXIST;

public class UpdateCustomerUseCase implements IUpdateCustomerUseCase {

    private final ICustomerRepositoryService customerRepositoryService;

    public UpdateCustomerUseCase(ICustomerRepositoryService customerRepositoryService) {
        this.customerRepositoryService = customerRepositoryService;
    }

    @Override
    public Customer updateCustomer(UpdateCustomerRequest updateCustomerRequest) throws DomainClientException {

        String customerId = updateCustomerRequest.customerId();
        String name = updateCustomerRequest.name();
        String surname = updateCustomerRequest.surname();
        String currentUser = updateCustomerRequest.currentUser();

        Customer customerToUpdate = this.customerRepositoryService.getActiveCustomerById(customerId)
                .orElseThrow(new DomainClientException(CUSTOMER_DOES_NOT_EXIST));

        Customer updatedCustomer = getUpdatedCustomer(customerId, name, surname, currentUser, customerToUpdate);

        return this.customerRepositoryService.saveCustomer(updatedCustomer);
    }

    private Customer getUpdatedCustomer(String customerId, String name, String surname, String currentUser, Customer customerToUpdate) {
        return new Customer(
                customerId,
                getName(name, customerToUpdate),
                getSurname(surname, customerToUpdate),
                customerToUpdate.getDocument(),
                customerToUpdate.getCreatedAt(),
                customerToUpdate.getCreatedBy(),
                Instant.now(),
                currentUser,
                customerToUpdate.getPhotoUrl(),
                CustomerState.UPDATED);
    }

    private String getSurname(String surname, Customer customerToUpdate) {
        return Optional.ofNullable(surname).filter(value -> !value.isBlank()).orElse(customerToUpdate.getSurname());
    }

    private String getName(String name, Customer customerToUpdate) {
        return Optional.ofNullable(name).filter(value -> !value.isBlank()).orElse(customerToUpdate.getName());
    }
}
