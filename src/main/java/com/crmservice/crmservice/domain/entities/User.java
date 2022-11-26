package com.crmservice.crmservice.domain.entities;

import com.crmservice.crmservice.domain.enums.Role;

import java.util.UUID;

public class User {
    private final UUID id;
    private final String username;
    private final String password;
    private final Role role;

    public User(UUID id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
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
}
