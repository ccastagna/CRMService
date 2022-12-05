package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.deletecustomer.DeleteCustomerRequest;
import com.crmservice.crmservice.domain.usecases.interfaces.IDeleteCustomerUseCase;
import com.crmservice.crmservice.infrastructure.drivers.logs.LogMessageBuilder;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static com.crmservice.crmservice.infrastructure.drivers.requesthandlers.InputValueEvaluator.isValidCustomerId;

public class DeleteCustomerRequestHandler extends BaseRequestHandler<Void, Void> {

    private final IDeleteCustomerUseCase deleteCustomerUseCase;

    private final Logger logger = LoggerFactory.getLogger(DeleteCustomerRequestHandler.class);

    public DeleteCustomerRequestHandler(IDeleteCustomerUseCase deleteCustomerUseCase) {
        this.deleteCustomerUseCase = deleteCustomerUseCase;
    }

    @Override
    public ResponseEntity<Void> handle(RequestDTO<Void> request) {

        ResponseEntity response;

        try {
            String currentUser = Optional.ofNullable(request.getContext(RequestContextKey.CURRENT_USER))
                    .orElseThrow();
            String customerToDelete = Optional.ofNullable(request.getContext(RequestContextKey.CUSTOMER_TO_DELETE))
                    .orElseThrow();

            if (!isValidCustomerId(customerToDelete)) {
                throw new IllegalArgumentException("Invalid customer id: " + customerToDelete);
            }

            DeleteCustomerRequest deleteCustomerRequest = new DeleteCustomerRequest(customerToDelete, currentUser);

            this.deleteCustomerUseCase.deleteCustomer(deleteCustomerRequest);

            response = HttpAdapterResponseBuilder.noContent();

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
