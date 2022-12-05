package com.crmservice.crmservice.infrastructure.drivers.requests.dtos;

import com.crmservice.crmservice.domain.usecases.updatecustomer.UpdateCustomerRequest;

public record UpdateCustomerRequestDTO(String name, String surname) {
    public UpdateCustomerRequest toUpdateCustomerRequest(String currentUser, String customerId) {
        return new UpdateCustomerRequest(customerId, name, surname, currentUser);
    }
}
