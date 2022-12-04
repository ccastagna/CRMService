package com.crmservice.crmservice.domain.interfaces;

import com.crmservice.crmservice.domain.entities.User;

import java.util.Optional;

public interface IUserRepositoryService {
    Optional<User> getActiveUserByUsername(String requestUsername);

    Optional<Object> getUserByUsername(String notNullName);

    User saveUser(User newUser);
}
