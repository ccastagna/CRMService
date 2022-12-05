package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.interfaces.IUpdateCustomerPhotoUseCase;
import com.crmservice.crmservice.domain.usecases.updatecustomerphoto.UpdateCustomerPhotoRequest;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.UpdateCustomerPhotoResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.crmservice.crmservice.infrastructure.drivers.requesthandlers.InputValueEvaluator.isValidCustomerId;
import static com.crmservice.crmservice.infrastructure.drivers.requesthandlers.InputValueEvaluator.isValidImageFormat;

public class UpdateCustomerPhotoRequestHandler extends BaseRequestHandler<Void, UpdateCustomerPhotoResponseDTO> {

    private final String ERROR_LOG_TEMPLATE = "{} with {}";

    private final IUpdateCustomerPhotoUseCase updateCustomerPhotoUseCase;

    private final Logger logger = LoggerFactory.getLogger(UpdateCustomerPhotoRequestHandler.class);

    public UpdateCustomerPhotoRequestHandler(IUpdateCustomerPhotoUseCase updateCustomerPhotoUseCase) {
        this.updateCustomerPhotoUseCase = updateCustomerPhotoUseCase;
    }

    @Override
    public ResponseEntity<UpdateCustomerPhotoResponseDTO> handle(RequestDTO<Void> request) {

        ResponseEntity response;

        try {
            String currentUser = Optional.of(request.getContext(RequestContextKey.CURRENT_USER)).orElseThrow();
            String customerId = request.getContext(RequestContextKey.CUSTOMER_TO_UPDATE);
            UpdateCustomerPhotoRequest updateCustomerPhotoRequest = new UpdateCustomerPhotoRequest(currentUser, customerId, request.getPhoto());

            evaluateRequestValues(updateCustomerPhotoRequest);

            Customer updateCustomerResponse = this.updateCustomerPhotoUseCase.updateCustomerPhoto(updateCustomerPhotoRequest);

            response = ResponseEntity.ok()
                    .body(UpdateCustomerPhotoResponseDTO.from(updateCustomerResponse));

        } catch (IllegalArgumentException ex) {
            response = HttpAdapterResponseBuilder.badRequest(ex.getMessage());
            logger.error(ERROR_LOG_TEMPLATE, ex.getMessage(), getStackTrace(ex.getStackTrace()));

        } catch (MultipartException ex) {
            response = HttpAdapterResponseBuilder.unsupportedMediaType(ex.getMessage());
            logger.error(ERROR_LOG_TEMPLATE, ex.getMessage(), getStackTrace(ex.getStackTrace()));

        } catch (DomainClientException ex) {
            response = HttpAdapterResponseBuilder.fromDomainException(ex);
            logger.error(ERROR_LOG_TEMPLATE, ex.getMessage(), getStackTrace(ex.getStackTrace()));

        } catch (Exception ex) {
            response = HttpAdapterResponseBuilder.internalServerError();
            logger.error(ERROR_LOG_TEMPLATE, ex.getMessage(), getStackTrace(ex.getStackTrace()));
        }

        return response;
    }

    private void evaluateRequestValues(UpdateCustomerPhotoRequest updateCustomerPhotoRequest) {
        String customerId = updateCustomerPhotoRequest.customerId();
        MultipartFile photo = updateCustomerPhotoRequest.photo();

        if (!isValidCustomerId(customerId)) {
            throw new IllegalArgumentException("Invalid customer id: " + customerId);
        }

        if(!isValidImageFormat(photo)) {
            throw new MultipartException("Unsupported image format.");
        }
    }

}
