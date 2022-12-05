package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.interfaces.IUpdateCustomerUseCase;
import com.crmservice.crmservice.domain.usecases.updatecustomer.UpdateCustomerRequest;
import com.crmservice.crmservice.infrastructure.drivers.logs.LogMessageBuilder;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.UpdateCustomerRequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.UpdateCustomerResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static com.crmservice.crmservice.infrastructure.drivers.requesthandlers.InputValueEvaluator.isValidCustomerId;
import static com.crmservice.crmservice.infrastructure.drivers.requesthandlers.InputValueEvaluator.isValidCustomerName;
import static com.crmservice.crmservice.infrastructure.drivers.requesthandlers.InputValueEvaluator.isValidCustomerSurname;

public class UpdateCustomerRequestHandler extends BaseRequestHandler<UpdateCustomerRequestDTO, UpdateCustomerResponseDTO> {

    private final IUpdateCustomerUseCase updateCustomerUseCase;

    private final Logger logger = LoggerFactory.getLogger(UpdateCustomerRequestHandler.class);

    public UpdateCustomerRequestHandler(IUpdateCustomerUseCase updateCustomerUseCase) {
        this.updateCustomerUseCase = updateCustomerUseCase;
    }

    @Override
    public ResponseEntity<UpdateCustomerResponseDTO> handle(RequestDTO<UpdateCustomerRequestDTO> request) {

        ResponseEntity response;

        try {
            String currentUser = Optional.of(request.getContext(RequestContextKey.CURRENT_USER)).orElseThrow();
            UpdateCustomerRequestDTO updateCustomerRequestDTO = getUpdateCustomerRequestDTO(request);
            String customerId = request.getContext(RequestContextKey.CUSTOMER_TO_UPDATE);
            UpdateCustomerRequest updateCustomerRequest = updateCustomerRequestDTO.toUpdateCustomerRequest(currentUser, customerId);

            evaluateRequestStringValues(updateCustomerRequest);

            Customer updateCustomerResponse = this.updateCustomerUseCase.updateCustomer(updateCustomerRequest);

            response = ResponseEntity.ok()
                    .body(UpdateCustomerResponseDTO.from(updateCustomerResponse));

        } catch (IllegalArgumentException ex) {
            response = HttpAdapterResponseBuilder.badRequest(ex.getMessage());
            logger.error(LogMessageBuilder.build(request.getRequestEntity(), ex.getMessage(), getStackTrace(ex.getStackTrace())));

        } catch (DomainClientException ex) {
            response = HttpAdapterResponseBuilder.fromDomainException(ex);
            logger.error(LogMessageBuilder.build(request.getRequestEntity(), ex.getMessage(), getStackTrace(ex.getStackTrace())));

        } catch (Exception ex) {
            response = HttpAdapterResponseBuilder.internalServerError();
            logger.error(LogMessageBuilder.build(request.getRequestEntity(), ex.getMessage(), getStackTrace(ex.getStackTrace())));
        }

        return response;
    }

    private void evaluateRequestStringValues(UpdateCustomerRequest updateCustomerRequest) {
        String name = updateCustomerRequest.name();
        String surname = updateCustomerRequest.surname();
        String customerId = updateCustomerRequest.customerId();
        if (name != null && !name.isBlank() && !isValidCustomerName(name)) {
            throw new IllegalArgumentException("Invalid customer name: " + name);
        }
        if (surname != null && !surname.isBlank() && !isValidCustomerSurname(surname)) {
            throw new IllegalArgumentException("Invalid customer surname: " + surname);
        }
        if (!isValidCustomerId(customerId)) {
            throw new IllegalArgumentException("Invalid customer id: " + customerId);
        }
    }

    private UpdateCustomerRequestDTO getUpdateCustomerRequestDTO(RequestDTO<UpdateCustomerRequestDTO> request) {
        return Optional.ofNullable(request.getRequestEntity().getBody())
                .orElseThrow();
    }

}
