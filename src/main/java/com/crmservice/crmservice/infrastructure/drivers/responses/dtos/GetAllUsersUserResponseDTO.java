package com.crmservice.crmservice.infrastructure.drivers.responses.dtos;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.enums.Role;

import java.util.UUID;

public record GetAllUsersUserResponseDTO(UUID id, String userName, Role role) {
    public static GetAllUsersUserResponseDTO from(User user) {
        return new GetAllUsersUserResponseDTO(user.getId(), user.getUsername(), user.getRole());
    }
}
