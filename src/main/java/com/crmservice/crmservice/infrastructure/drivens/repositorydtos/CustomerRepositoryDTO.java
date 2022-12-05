package com.crmservice.crmservice.infrastructure.drivens.repositorydtos;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.enums.CustomerState;

import java.time.Instant;

public record CustomerRepositoryDTO(String id, String name, String surname, DocumentRepositoryDTO document,
                                    String createdAt, String createdBy, String lastUpdatedAt, String lastUpdatedBy,
                                    String photoUrl, CustomerState state) {

    public static CustomerRepositoryDTO from(Customer customer) {
        return new CustomerRepositoryDTO(
                customer.getId(),
                customer.getName(),
                customer.getSurname(),
                DocumentRepositoryDTO.from(customer.getDocument()),
                customer.getCreatedAt().toString(),
                customer.getCreatedBy(),
                customer.getLastUpdatedAt().toString(),
                customer.getLastUpdatedBy(),
                customer.getPhotoUrl(),
                customer.getState()
        );
    }

    public Customer toEntity() {
        return new Customer(
                id,
                name,
                surname,
                document.toEntity(),
                Instant.parse(createdAt),
                createdBy,
                Instant.parse(lastUpdatedAt),
                lastUpdatedBy,
                photoUrl,
                state
        );
    }

    public boolean isNotDeleted() {
        return this.state != CustomerState.DELETED;
    }
}
