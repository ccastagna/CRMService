package com.crmservice.crmservice.infrastructure.drivers.responses.dtos;

import com.crmservice.crmservice.domain.entities.Customer;

import java.util.List;

public record GetAllCustomersResponseDTO(List<GetCustomerResponseDTO> users) {
    public static GetAllCustomersResponseDTO from(List<Customer> getAllUsersResponse) {
        List<GetCustomerResponseDTO> customers = getAllUsersResponse
                .stream()
                .map(customer -> GetCustomerResponseDTO.from(customer))
                .toList();
        return new GetAllCustomersResponseDTO(customers);
    }
}

