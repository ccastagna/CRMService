package com.crmservice.crmservice.domain.services.credentialsvalidators;

public interface IUserCredentialsValidator {
    boolean isUsernameValid(String name);

    boolean isUsernameDuplicated(String name);

    boolean isUserPasswordSecure(String password);

    boolean isRequestUsernameValid(String username, String requestUsername);

    boolean isRequestUserPasswordValid(String password, String requestPassword);
}
