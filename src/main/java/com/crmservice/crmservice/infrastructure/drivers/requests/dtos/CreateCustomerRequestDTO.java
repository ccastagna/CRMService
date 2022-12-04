package com.crmservice.crmservice.infrastructure.drivers.requests.dtos;

public record CreateCustomerRequestDTO(String name, String surname, DocumentRequestDTO document) {
}
