package com.crmservice.crmservice.infrastructure.drivers.controllers;

import com.crmservice.crmservice.domain.enums.UseCase;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.IRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.UpdateCustomerPhotoResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.CUSTOMER_TO_UPDATE;
import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.USE_CASE;

@RestController
@Scope("prototype")
public class UpdateCustomerPhotoController {

    private final IRequestHandler<Void, Void> ipHeaderHandler;

    public UpdateCustomerPhotoController(
            @Qualifier("IPHeaderHandler") IRequestHandler<Void, Void> ipHeaderHandler,
            @Qualifier("BearerAuthHeaderHandler") IRequestHandler<Void, Void> bearerAuthenticationHeaderHandler,
            IRequestHandler<Void, UpdateCustomerPhotoResponseDTO> updateCustomerPhotoRequestHandler
    ) {
        this.ipHeaderHandler = ipHeaderHandler;
        this.ipHeaderHandler
                .setNext(bearerAuthenticationHeaderHandler)
                .setNext(updateCustomerPhotoRequestHandler);
    }

    @PutMapping("/v1/customers/{customerId}/photos")
    public ResponseEntity<UpdateCustomerPhotoResponseDTO> updateCustomerPhoto(HttpServletRequest request, @RequestPart(value = "file") MultipartFile photo, @PathVariable String customerId) {
        RequestDTO requestWithContext = new RequestDTO(request, photo);
        requestWithContext.setContext(USE_CASE, UseCase.UPDATE_CUSTOMER_PHOTO.name());
        requestWithContext.setContext(CUSTOMER_TO_UPDATE, customerId);

        return this.ipHeaderHandler.handle(requestWithContext);
    }
}
