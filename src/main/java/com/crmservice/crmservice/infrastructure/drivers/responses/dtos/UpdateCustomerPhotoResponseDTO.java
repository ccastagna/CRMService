package com.crmservice.crmservice.infrastructure.drivers.responses.dtos;

import com.crmservice.crmservice.domain.entities.Customer;

public record UpdateCustomerPhotoResponseDTO(String photoUrl) {
    public static UpdateCustomerPhotoResponseDTO from(Customer updateCustomerResponse) {
        return new UpdateCustomerPhotoResponseDTO(updateCustomerResponse.getPhotoUrl());
    }
}
