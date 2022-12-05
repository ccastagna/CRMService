package com.crmservice.crmservice.infrastructure.drivers.responses;

import com.crmservice.crmservice.domain.responses.DomainErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.List;

enum HttpAdapterErrorResponse {

    //400
    BAD_REQUEST(
            HttpStatus.BAD_REQUEST,
            List.of(
                    DomainErrorResponse.MALFORMED_USERNAME,
                    DomainErrorResponse.MALFORMED_PASSWORD),
            "Bad Request."
    ),

    //401
    UNAUTHORIZED_TOKEN(
            HttpStatus.UNAUTHORIZED,
            List.of(
                    DomainErrorResponse.INVALID_PASSWORD_CREDENTIAL,
                    DomainErrorResponse.INVALID_USERNAME_CREDENTIAL
            ),
            "Specified token does not match."
    ),

    // 404
    USER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            List.of(DomainErrorResponse.USER_DOES_NOT_EXIST),
            "Requested User does not exist."
    ),

    CUSTOMER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            List.of(DomainErrorResponse.CUSTOMER_DOES_NOT_EXIST),
            "Requested Customer does not exist."
    ),

    // 422
    MALFORMED_EXPECTED_DATA(
            HttpStatus.UNPROCESSABLE_ENTITY,
            List.of(
                    DomainErrorResponse.ALREADY_EXISTENT_USER,
                    DomainErrorResponse.ALREADY_EXISTENT_CUSTOMER
            ),
            "Malformed expected data."
    ),

    // 429
    TOO_MANY_REQUESTS(
            HttpStatus.TOO_MANY_REQUESTS,
            List.of(
                    DomainErrorResponse.TOO_MANY_LOGIN_ATTEMPTS
            ),
            "Too many requests."
    ),

    // 500
    INTERNAL_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            List.of(),
            "Unexpected API error."
    );

    private final HttpStatus status;
    private final List<DomainErrorResponse> domainErrorType;
    private final String responseMessage;

    HttpAdapterErrorResponse(HttpStatus status, List<DomainErrorResponse> domainErrorType, String responseMessage) {
        this.status = status;
        this.domainErrorType = domainErrorType;
        this.responseMessage = responseMessage;
    }

    public List<DomainErrorResponse> getDomainErrorType() {
        return this.domainErrorType;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

}
