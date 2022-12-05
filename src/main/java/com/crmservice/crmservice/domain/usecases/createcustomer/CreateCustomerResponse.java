package com.crmservice.crmservice.domain.usecases.createcustomer;

import com.crmservice.crmservice.domain.entities.Document;

public record CreateCustomerResponse(String id, String name, String surname, Document document,
                                     String createdAt, String createdBy) {
}
