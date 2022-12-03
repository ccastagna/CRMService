package com.crmservice.crmservice.infrastructure.drivens.repositorydtos;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.enums.Role;
import com.crmservice.crmservice.domain.enums.UserState;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;

public record UserRepositoryDTO(UUID id, @Indexed String username, String password, Role role, UserState state) {
    public static UserRepositoryDTO from(User newUser) {
        return new UserRepositoryDTO(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getPassword(),
                newUser.getRole(),
                newUser.getState()
        );
    }

    public User toEntity() {
        return new User(id, username, password, role, state);
    }
}
