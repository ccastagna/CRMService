package com.crmservice.crmservice.infrastructure.drivers.responses.dtos;

import com.crmservice.crmservice.domain.usecases.createcustomer.CreateCustomerResponse;

public record CreateCustomerResponseDTO(String id, String name, String surname, DocumentResponseDTO document,
                                        String createdAt, String createdBy) {
    public static CreateCustomerResponseDTO from(CreateCustomerResponse createCustomerResponse) {
        return new CreateCustomerResponseDTO(createCustomerResponse.id(),
                createCustomerResponse.name(),
                createCustomerResponse.surname(),
                DocumentResponseDTO.from(createCustomerResponse.document()),
                createCustomerResponse.createdAt(),
                createCustomerResponse.createdBy());
    }
}
