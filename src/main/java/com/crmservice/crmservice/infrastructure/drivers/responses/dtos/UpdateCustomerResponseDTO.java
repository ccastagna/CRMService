package com.crmservice.crmservice.infrastructure.drivers.responses.dtos;

import com.crmservice.crmservice.domain.entities.Customer;

public record UpdateCustomerResponseDTO(String id, String name, String surname, DocumentResponseDTO document,
                                        String createdAt, String createdBy, String lastUpdatedAt, String lastUpdatedBy,
                                        String photoUrl) {

    public static UpdateCustomerResponseDTO from(Customer customer) {
        return new UpdateCustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getSurname(),
                DocumentResponseDTO.from(customer.getDocument()),
                customer.getCreatedAt().toString(),
                customer.getCreatedBy(),
                customer.getLastUpdatedAt().toString(),
                customer.getLastUpdatedBy(),
                customer.getPhotoUrl()
        );
    }
}
