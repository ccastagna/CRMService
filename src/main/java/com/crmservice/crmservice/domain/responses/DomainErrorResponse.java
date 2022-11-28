package com.crmservice.crmservice.domain.responses;

public enum DomainErrorResponse {
    USER_DOES_NOT_EXIST("Requested user does not exist."),
    MALFORMED_USERNAME("Malformed Username."),
    MALFORMED_PASSWORD("Malformed Password."),
    TOO_MANY_LOGIN_ATTEMPTS("Too many login attempts."),
    INVALID_ACCESS_TOKEN("Invalid access token."),
    USERNAME_DOES_NOT_MATCH("The specified username does not match with any existing user.");

    private final String message;

    DomainErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
