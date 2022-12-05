package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.createcustomer.CreateCustomerRequest;
import com.crmservice.crmservice.domain.usecases.createcustomer.CreateCustomerResponse;
import com.crmservice.crmservice.domain.usecases.interfaces.ICreateCustomerUseCase;
import com.crmservice.crmservice.infrastructure.drivers.logs.LogMessageBuilder;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.CreateCustomerRequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.CreateCustomerResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static com.crmservice.crmservice.infrastructure.drivers.requesthandlers.InputValueEvaluator.evaluateCustomerDocumentNumber;
import static com.crmservice.crmservice.infrastructure.drivers.requesthandlers.InputValueEvaluator.evaluateCustomerName;
import static com.crmservice.crmservice.infrastructure.drivers.requesthandlers.InputValueEvaluator.evaluateCustomerSurname;

public class CreateCustomerRequestHandler extends BaseRequestHandler<CreateCustomerRequestDTO, CreateCustomerResponseDTO> {

    private final ICreateCustomerUseCase createCustomerUseCase;

    private final Logger logger = LoggerFactory.getLogger(CreateCustomerRequestHandler.class);

    public CreateCustomerRequestHandler(ICreateCustomerUseCase createCustomerUseCase) {
        this.createCustomerUseCase = createCustomerUseCase;
    }

    @Override
    public ResponseEntity<CreateCustomerResponseDTO> handle(RequestDTO<CreateCustomerRequestDTO> request) {

        ResponseEntity response;

        try {
            CreateCustomerRequestDTO createCustomerRequestDTO = getCreateCustomerRequestDTO(request);

            evaluateRequestStringValues(createCustomerRequestDTO);

            String currentUser = Optional.of(request.getContext(RequestContextKey.CURRENT_USER)).orElseThrow();

            CreateCustomerRequest createCustomerRequest = createCustomerRequestDTO.toCreateCustomerRequest(currentUser);

            CreateCustomerResponse createCustomerResponse = this.createCustomerUseCase.createCustomer(createCustomerRequest);

            response = ResponseEntity.status(HttpStatus.CREATED)
                    .body(CreateCustomerResponseDTO.from(createCustomerResponse));

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

    private void evaluateRequestStringValues(CreateCustomerRequestDTO createCustomerRequestDTO) {
        evaluateCustomerName(createCustomerRequestDTO.name());
        evaluateCustomerSurname(createCustomerRequestDTO.surname());
        Optional.of(createCustomerRequestDTO.document())
                .ifPresent(document -> evaluateCustomerDocumentNumber(document.number()));
    }

    private CreateCustomerRequestDTO getCreateCustomerRequestDTO(RequestDTO<CreateCustomerRequestDTO> request) {
        return Optional.ofNullable(request.getRequestEntity().getBody())
                .orElseThrow();
    }

}
