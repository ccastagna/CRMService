package com.crmservice.crmservice.infrastructure.drivers.responses.dtos;

import com.crmservice.crmservice.domain.enums.Role;
import com.crmservice.crmservice.domain.usecases.createuser.CreateUserResponse;

import java.util.UUID;

public record CreateUserResponseDTO(UUID id, String userName, Role role) {
    public static CreateUserResponseDTO from(CreateUserResponse createUserResponse) {
        return new CreateUserResponseDTO(createUserResponse.id(), createUserResponse.userName(), createUserResponse.role());
    }
}
