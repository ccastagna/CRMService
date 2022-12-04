package com.crmservice.crmservice.infrastructure.drivers.controllers;

import com.crmservice.crmservice.domain.enums.UseCase;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.IRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.CreateUserRequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.CreateUserResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.USE_CASE;

@RestController
@Scope("prototype")
public class CreateUserController {

    private final IRequestHandler<Void, Void> ipHeaderHandler;

    public CreateUserController(
            @Qualifier("IPHeaderHandler") IRequestHandler<Void, Void> ipHeaderHandler,
            @Qualifier("BearerAuthHeaderHandler") IRequestHandler<Void, Void> bearerAuthenticationHeaderHandler,
            IRequestHandler<CreateUserRequestDTO, CreateUserResponseDTO> createUserRequestHandler
    ) {
        this.ipHeaderHandler = ipHeaderHandler;
        this.ipHeaderHandler
                .setNext(bearerAuthenticationHeaderHandler)
                .setNext(createUserRequestHandler);
    }

    @PostMapping("/v1/users")
    public ResponseEntity<CreateUserResponseDTO> createUser(RequestEntity<CreateUserRequestDTO> request) {
        RequestDTO requestWithContext = new RequestDTO(request);
        requestWithContext.setContext(USE_CASE, UseCase.CREATE_USER.name());

        return this.ipHeaderHandler.handle(requestWithContext);
    }
}
