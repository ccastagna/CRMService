package com.crmservice.crmservice.domain.interfaces;

import com.crmservice.crmservice.domain.entities.User;

import java.util.Optional;

public interface IUserRepositoryService {
    Optional<User> getActiveUserByUsername(String requestUsername);

    User createUser(User newUser);

    void deleteUser(User userToDelete, String currentUserUsername);
}
