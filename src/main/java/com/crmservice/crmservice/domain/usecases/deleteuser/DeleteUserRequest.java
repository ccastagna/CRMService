package com.crmservice.crmservice.domain.usecases.deleteuser;

public record DeleteUserRequest(String currentUser, String usernameToDelete) {}
