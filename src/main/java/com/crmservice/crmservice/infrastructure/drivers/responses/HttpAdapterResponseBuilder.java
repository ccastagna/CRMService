package com.crmservice.crmservice.infrastructure.drivers.responses;

import com.crmservice.crmservice.domain.responses.DomainClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterErrorResponse.INTERNAL_ERROR;
import static com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterErrorResponse.MALFORMED_EXPECTED_DATA;
import static com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterErrorResponse.UNAUTHORIZED_TOKEN;

public class HttpAdapterResponseBuilder {

    private HttpAdapterResponseBuilder() {
    }

    public static ResponseEntity fromDomainException(DomainClientException ex) {
        HttpAdapterErrorResponse httpErrorResponse = Arrays.stream(HttpAdapterErrorResponse.values())
                .filter(value -> value.getDomainErrorType().contains(ex.getDomainErrorType()))
                .findFirst()
                .orElse(INTERNAL_ERROR);

        return ResponseEntity.status(httpErrorResponse.getStatus())
                .body(httpErrorResponse.getResponseMessage());
    }

    public static ResponseEntity internalServerError() {
        return ResponseEntity.status(INTERNAL_ERROR.getStatus())
                .body(INTERNAL_ERROR.getResponseMessage());
    }

    public static ResponseEntity malformedExpectedData() {
        return ResponseEntity.status(MALFORMED_EXPECTED_DATA.getStatus())
                .body(MALFORMED_EXPECTED_DATA.getResponseMessage());
    }

    public static ResponseEntity unauthorizedToken() {
        return ResponseEntity.status(UNAUTHORIZED_TOKEN.getStatus())
                .body(UNAUTHORIZED_TOKEN.getResponseMessage());
    }

    public static ResponseEntity noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public static ResponseEntity badRequest(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    public static ResponseEntity forbidden(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(message);
    }

    public static ResponseEntity unsupportedMediaType(String message) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(message);
    }


}
