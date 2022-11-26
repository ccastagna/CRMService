package com.crmservice.crmservice.domain.responses;

public enum DomainErrorResponse {
    USER_DOES_NOT_EXIST("Requested user does not exist."),
    MALFORMED_CREDENTIALS("Malformed credentials."),
    MALFORMED_USERNAME("Malformed Username."),
    MALFORMED_PASSWORD("Malformed Password."),
    TOO_MANY_LOGIN_ATTEMPTS("Too many login attempts.");

    private final String message;

    DomainErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
