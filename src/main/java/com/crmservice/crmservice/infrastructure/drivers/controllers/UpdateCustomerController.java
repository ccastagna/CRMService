package com.crmservice.crmservice.infrastructure.drivers.controllers;

import com.crmservice.crmservice.domain.enums.UseCase;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.IRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.UpdateCustomerRequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.UpdateCustomerResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.CUSTOMER_TO_UPDATE;
import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.USE_CASE;

@RestController
@Scope("prototype")
public class UpdateCustomerController {

    private final IRequestHandler<Void, Void> ipHeaderHandler;

    public UpdateCustomerController(
            @Qualifier("IPHeaderHandler") IRequestHandler<Void, Void> ipHeaderHandler,
            @Qualifier("BearerAuthHeaderHandler") IRequestHandler<Void, Void> bearerAuthenticationHeaderHandler,
            IRequestHandler<UpdateCustomerRequestDTO, UpdateCustomerResponseDTO> updateCustomerRequestHandler
    ) {
        this.ipHeaderHandler = ipHeaderHandler;
        this.ipHeaderHandler
                .setNext(bearerAuthenticationHeaderHandler)
                .setNext(updateCustomerRequestHandler);
    }

    @PutMapping("/v1/customers/{customerId}")
    public ResponseEntity<UpdateCustomerResponseDTO> updateCustomer(RequestEntity<UpdateCustomerRequestDTO> request, @PathVariable String customerId) {
        RequestDTO requestWithContext = new RequestDTO(request);
        requestWithContext.setContext(USE_CASE, UseCase.UPDATE_CUSTOMER.name());
        requestWithContext.setContext(CUSTOMER_TO_UPDATE, customerId);

        return this.ipHeaderHandler.handle(requestWithContext);
    }
}
