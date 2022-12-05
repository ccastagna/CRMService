package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.interfaces.IGetCustomerUseCase;
import com.crmservice.crmservice.infrastructure.drivers.logs.LogMessageBuilder;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.GetCustomerResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static com.crmservice.crmservice.infrastructure.drivers.requesthandlers.InputValueEvaluator.isValidCustomerId;

public class GetCustomerRequestHandler extends BaseRequestHandler<Void, GetCustomerResponseDTO> {

    private final IGetCustomerUseCase getCustomerUseCase;

    private final Logger logger = LoggerFactory.getLogger(GetCustomerRequestHandler.class);

    public GetCustomerRequestHandler(IGetCustomerUseCase getCustomerUseCase) {
        this.getCustomerUseCase = getCustomerUseCase;
    }

    @Override
    public ResponseEntity<GetCustomerResponseDTO> handle(RequestDTO<Void> request) {

        ResponseEntity response;

        try {

            String customerId = Optional.ofNullable(request.getContext(RequestContextKey.CUSTOMER_ID)).orElseThrow();

            if (!isValidCustomerId(customerId)) {
                throw new IllegalArgumentException("Invalid customer id: " + customerId);
            }

            Customer getCustomerResponse = this.getCustomerUseCase.getCustomer(customerId);

            response = ResponseEntity.ok()
                    .body(GetCustomerResponseDTO.from(getCustomerResponse));

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

}
