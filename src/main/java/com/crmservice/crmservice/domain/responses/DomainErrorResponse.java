package com.crmservice.crmservice.domain.responses;

public enum DomainErrorResponse {

    ALREADY_EXISTENT_USER("The specified username already exists"),
    USER_DOES_NOT_EXIST("Requested user does not exist."),
    MALFORMED_USERNAME("Malformed Username."),
    MALFORMED_PASSWORD("Malformed Password."),
    TOO_MANY_LOGIN_ATTEMPTS("Too many login attempts."),
    INVALID_USERNAME_CREDENTIAL("Invalid username credential."),
    INVALID_PASSWORD_CREDENTIAL("Invalid password credential."),
    ALREADY_EXISTENT_CUSTOMER("The specified customer already exists"),
    CUSTOMER_DOES_NOT_EXIST("Requested customer does not exist.");

    private final String message;

    DomainErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
