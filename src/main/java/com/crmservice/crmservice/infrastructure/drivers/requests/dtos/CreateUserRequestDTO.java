package com.crmservice.crmservice.infrastructure.drivers.requests.dtos;

import com.crmservice.crmservice.domain.enums.Role;
import com.crmservice.crmservice.domain.usecases.createuser.CreateUserRequest;

public record CreateUserRequestDTO(String userName, String password, Role role) {
    public CreateUserRequest toCreateUserRequest() {
        return new CreateUserRequest(userName, password, role);
    }
}
