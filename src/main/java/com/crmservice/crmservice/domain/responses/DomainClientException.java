package com.crmservice.crmservice.domain.responses;

import java.util.function.Supplier;

public class DomainClientException extends Exception implements Supplier<DomainClientException> {

    private final DomainErrorResponse domainErrorType;

    public DomainClientException(DomainErrorResponse domainErrorType) {
        super(domainErrorType.getMessage());
        this.domainErrorType = domainErrorType;
    }

    public DomainErrorResponse getDomainErrorType() {
        return this.domainErrorType;
    }

    @Override
    public DomainClientException get() {
        return this;
    }
}
