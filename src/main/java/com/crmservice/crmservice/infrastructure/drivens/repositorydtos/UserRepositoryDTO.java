package com.crmservice.crmservice.infrastructure.drivens.repositorydtos;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.enums.Role;

import java.util.UUID;

public record UserRepositoryDTO(UUID id, String username, String password, Role role, RepositoryDTOState state) {
    public User toEntity() {
        return new User(id, username, password, role);
    }
}
