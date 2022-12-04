package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseRequestHandler<T, R> implements IRequestHandler<T, R> {

    private IRequestHandler<T, R> next;

    public IRequestHandler setNext(IRequestHandler handler) {
        this.next = handler;
        return handler;
    }

    @Override
    public ResponseEntity handle(RequestDTO<T> request) {
        return Optional.ofNullable(next)
                .map(nextHandler -> nextHandler.handle(request))
                .orElse(HttpAdapterResponseBuilder.noContent());
    }

    protected String getStackTrace(StackTraceElement[] stackTrace) {
        return Arrays.stream(stackTrace)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n\t\t"));
    }
}
