package com.crmservice.crmservice.infrastructure.drivers.controllers;

import com.crmservice.crmservice.domain.enums.UseCase;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.IRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.GetAllUsersResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.USE_CASE;

@RestController
@Scope("prototype")
public class GetAllUsersController {

    private final IRequestHandler<Void, Void> ipHeaderHandler;

    public GetAllUsersController(
            @Qualifier("IPHeaderHandler") IRequestHandler<Void, Void> ipHeaderHandler,
            @Qualifier("BearerAuthHeaderHandler") IRequestHandler<Void, Void> bearerAuthenticationHeaderHandler,
            IRequestHandler<Void, GetAllUsersResponseDTO> getAllUsersRequestHandler
    ) {
        this.ipHeaderHandler = ipHeaderHandler;
        this.ipHeaderHandler
                .setNext(bearerAuthenticationHeaderHandler)
                .setNext(getAllUsersRequestHandler);
    }

    @GetMapping("/v1/users")
    public ResponseEntity<GetAllUsersResponseDTO> createUser(RequestEntity<Void> request) {
        RequestDTO requestWithContext = new RequestDTO(request);
        requestWithContext.setContext(USE_CASE, UseCase.GET_ALL_USERS.name());

        return this.ipHeaderHandler.handle(requestWithContext);
    }
}
