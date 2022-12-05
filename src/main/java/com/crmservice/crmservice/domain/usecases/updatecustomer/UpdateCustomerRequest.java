package com.crmservice.crmservice.domain.usecases.updatecustomer;

public record UpdateCustomerRequest(String customerId, String name, String surname, String currentUser) {
}
