package com.crmservice.crmservice.domain.usecases.deletecustomer;

public record DeleteCustomerRequest(String customerToDelete, String currentUser) {
}
