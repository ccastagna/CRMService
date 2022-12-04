package com.crmservice.crmservice.infrastructure.drivers.responses.dtos;

import com.crmservice.crmservice.domain.enums.Role;
import com.crmservice.crmservice.domain.usecases.updateuser.UpdateUserResponse;

import java.util.UUID;

public record UpdateUserResponseDTO(UUID id, String userName, Role role) {
    public static UpdateUserResponseDTO from(UpdateUserResponse updateUserResponse) {
        return new UpdateUserResponseDTO(updateUserResponse.id(), updateUserResponse.userName(), updateUserResponse.role());
    }
}
