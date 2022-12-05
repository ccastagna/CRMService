package com.crmservice.crmservice.infrastructure.drivers.requests.dtos;

import com.crmservice.crmservice.domain.usecases.createcustomer.CreateCustomerRequest;

public record CreateCustomerRequestDTO(String name, String surname, DocumentRequestDTO document) {
    public CreateCustomerRequest toCreateCustomerRequest(String currentUser) {
        return new CreateCustomerRequest(name, surname, document.ToEntity(), currentUser);
    }
}
