package com.crmservice.crmservice.infrastructure.drivers.controllers;

import com.crmservice.crmservice.domain.enums.UseCase;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.IRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.CUSTOMER_TO_DELETE;
import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.USERNAME_TO_DELETE;
import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.USE_CASE;

@RestController
@Scope("prototype")
public class DeleteCustomerController {

    private final IRequestHandler<Void, Void> ipHeaderHandler;

    public DeleteCustomerController(
            @Qualifier("IPHeaderHandler") IRequestHandler<Void, Void> ipHeaderHandler,
            @Qualifier("BearerAuthHeaderHandler") IRequestHandler<Void, Void> bearerAuthenticationHeaderHandler,
            @Qualifier("DeleteCustomerRequestHandler") IRequestHandler<Void, Void> deleteCustomerRequestHandler
    ) {
        this.ipHeaderHandler = ipHeaderHandler;
        this.ipHeaderHandler
                .setNext(bearerAuthenticationHeaderHandler)
                .setNext(deleteCustomerRequestHandler);
    }

    @DeleteMapping("/v1/customers/{customerId}")
    public ResponseEntity<Void> deleteCustomer(RequestEntity<Void> request, @PathVariable String customerId) {
        RequestDTO requestWithContext = new RequestDTO(request);
        requestWithContext.setContext(USE_CASE, UseCase.DELETE_CUSTOMER.name());
        requestWithContext.setContext(CUSTOMER_TO_DELETE, customerId);

        return this.ipHeaderHandler.handle(requestWithContext);
    }
}
