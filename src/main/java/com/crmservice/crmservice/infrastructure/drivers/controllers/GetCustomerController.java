package com.crmservice.crmservice.infrastructure.drivers.controllers;

import com.crmservice.crmservice.domain.enums.UseCase;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.IRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.GetAllUsersResponseDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.GetCustomerResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.CUSTOMER_ID;
import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.CUSTOMER_TO_DELETE;
import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.USE_CASE;

@RestController
@Scope("prototype")
public class GetCustomerController {

    private final IRequestHandler<Void, Void> ipHeaderHandler;

    public GetCustomerController(
            @Qualifier("IPHeaderHandler") IRequestHandler<Void, Void> ipHeaderHandler,
            @Qualifier("BearerAuthHeaderHandler") IRequestHandler<Void, Void> bearerAuthenticationHeaderHandler,
            IRequestHandler<Void, GetCustomerResponseDTO> getCustomerRequestHandler
    ) {
        this.ipHeaderHandler = ipHeaderHandler;
        this.ipHeaderHandler
                .setNext(bearerAuthenticationHeaderHandler)
                .setNext(getCustomerRequestHandler);
    }

    @GetMapping("/v1/customers/{customerId}")
    public ResponseEntity<GetCustomerResponseDTO> getCustomer(RequestEntity<Void> request, @PathVariable String customerId) {
        RequestDTO requestWithContext = new RequestDTO(request);
        requestWithContext.setContext(USE_CASE, UseCase.GET_CUSTOMER.name());
        requestWithContext.setContext(CUSTOMER_ID, customerId);

        return this.ipHeaderHandler.handle(requestWithContext);
    }
}
