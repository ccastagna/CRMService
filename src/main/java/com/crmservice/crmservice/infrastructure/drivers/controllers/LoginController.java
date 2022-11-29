package com.crmservice.crmservice.infrastructure.drivers.controllers;

import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.IRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final IRequestHandler<Void, Void> basicAuthenticationHeaderHandler;

    public LoginController(
            @Qualifier("BasicAuthHeaderHandler") IRequestHandler<Void, Void> basicAuthenticationHeaderHandler,
            @Qualifier("IPHeaderHandler") IRequestHandler<Void, Void> ipHeaderHandler,
            IRequestHandler<Void, LoginResponseDTO> loginRequestHandler
    ) {
        this.basicAuthenticationHeaderHandler = basicAuthenticationHeaderHandler;
        this.basicAuthenticationHeaderHandler
                .setNext(ipHeaderHandler)
                .setNext(loginRequestHandler);
    }

    @GetMapping("/v1/login")
    public ResponseEntity<LoginResponseDTO> login(RequestEntity<Void> request) {
        return this.basicAuthenticationHeaderHandler.handle(new RequestDTO(request));
    }
}
