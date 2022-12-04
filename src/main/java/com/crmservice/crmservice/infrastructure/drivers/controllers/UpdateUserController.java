package com.crmservice.crmservice.infrastructure.drivers.controllers;

import com.crmservice.crmservice.domain.enums.UseCase;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.IRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.UpdateUserRequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.UpdateUserResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.USERNAME_TO_UPDATE;
import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.USE_CASE;

@RestController
@Scope("prototype")
public class UpdateUserController {

    private final IRequestHandler<Void, Void> ipHeaderHandler;

    public UpdateUserController(
            @Qualifier("IPHeaderHandler") IRequestHandler<Void, Void> ipHeaderHandler,
            @Qualifier("BearerAuthHeaderHandler") IRequestHandler<Void, Void> bearerAuthenticationHeaderHandler,
            IRequestHandler<UpdateUserRequestDTO, UpdateUserResponseDTO> updateUserRequestHandler
    ) {
        this.ipHeaderHandler = ipHeaderHandler;
        this.ipHeaderHandler
                .setNext(bearerAuthenticationHeaderHandler)
                .setNext(updateUserRequestHandler);
    }

    @PutMapping("/v1/users/{username}")
    public ResponseEntity<UpdateUserResponseDTO> updateUser(RequestEntity<UpdateUserRequestDTO> request, @PathVariable String username) {
        RequestDTO requestWithContext = new RequestDTO(request);
        requestWithContext.setContext(USE_CASE, UseCase.UPDATE_USER.name());
        requestWithContext.setContext(USERNAME_TO_UPDATE, username);

        return this.ipHeaderHandler.handle(requestWithContext);
    }
}
