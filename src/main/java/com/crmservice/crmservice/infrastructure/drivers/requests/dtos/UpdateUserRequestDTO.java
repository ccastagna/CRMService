package com.crmservice.crmservice.infrastructure.drivers.requests.dtos;

import com.crmservice.crmservice.domain.enums.Role;
import com.crmservice.crmservice.domain.usecases.createuser.CreateUserRequest;
import com.crmservice.crmservice.domain.usecases.updateuser.UpdateUserRequest;

public record UpdateUserRequestDTO(String password, Role role) {
    public CreateUserRequest toCreateUserRequest(String username) {
        return new UpdateUserRequest(username, password, role);
    }
}
