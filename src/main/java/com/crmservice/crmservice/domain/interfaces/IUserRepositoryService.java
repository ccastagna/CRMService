package com.crmservice.crmservice.domain.interfaces;

import com.crmservice.crmservice.domain.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepositoryService {
    Optional<User> getActiveUserByUsername(String requestUsername);

    Optional<User> getUserByUsername(String notNullName);

    User saveUser(User newUser);

    List<User> getAllActiveUsers();
}
