package com.crmservice.crmservice.domain.entities;

import com.crmservice.crmservice.domain.enums.Role;
import com.crmservice.crmservice.domain.enums.UserState;

import java.util.Optional;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String username;
    private final String password;
    private final Role role;
    private final UserState state;

    public User(String username, String password, Role role) {
        this(null, username, password, role, null);
    }

    public User(UUID id, String username, String password, Role role, UserState state) {
        this.id = Optional.ofNullable(id).orElse(UUID.randomUUID());
        this.username = username;
        this.password = password;
        this.role = role;
        this.state = Optional.ofNullable(state).orElse(UserState.ACTIVE);
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public UserState getState() {
        return state;
    }
}
