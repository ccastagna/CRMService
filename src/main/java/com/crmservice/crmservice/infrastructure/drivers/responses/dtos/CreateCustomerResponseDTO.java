package com.crmservice.crmservice.infrastructure.drivers.responses.dtos;

public record CreateCustomerResponseDTO(String id, String name, String surname, DocumentResponseDTO document,
                                        String createdAt, String createdBy) {
}
