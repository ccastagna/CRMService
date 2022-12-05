package com.crmservice.crmservice.domain.usecases.createcustomer;

import com.crmservice.crmservice.domain.entities.Document;

public record CreateCustomerRequest(String name, String surname, Document document, String currentUser) {
}
