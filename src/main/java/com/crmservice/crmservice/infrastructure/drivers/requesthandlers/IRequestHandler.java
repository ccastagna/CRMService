package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import org.springframework.http.ResponseEntity;

public interface IRequestHandler<T, R> {

    IRequestHandler setNext(IRequestHandler handler);
    ResponseEntity<R> handle(RequestDTO<T> request);
}
