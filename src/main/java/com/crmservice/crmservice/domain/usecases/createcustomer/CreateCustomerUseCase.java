package com.crmservice.crmservice.domain.usecases.createcustomer;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.entities.Document;
import com.crmservice.crmservice.domain.enums.CustomerState;
import com.crmservice.crmservice.domain.interfaces.ICustomerRepositoryService;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.responses.DomainErrorResponse;
import com.crmservice.crmservice.domain.usecases.interfaces.ICreateCustomerUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.time.Instant;

public class CreateCustomerUseCase implements ICreateCustomerUseCase {

    private static final String CUSTOMER_ID_TEMPLATE = "{0}:{1}";
    private final Logger logger = LoggerFactory.getLogger(CreateCustomerUseCase.class);
    private final ICustomerRepositoryService customerRepositoryService;

    public CreateCustomerUseCase(ICustomerRepositoryService customerRepositoryService) {
        this.customerRepositoryService = customerRepositoryService;
    }

    @Override
    public CreateCustomerResponse createCustomer(CreateCustomerRequest createCustomerRequest) throws DomainClientException {

        String customerId = getCustomerId(createCustomerRequest.document());

        if (customerAlreadyExists(customerId)) {
            throw new DomainClientException(DomainErrorResponse.ALREADY_EXISTENT_CUSTOMER);
        }

        Customer newCustomer = getNewCustomer(createCustomerRequest, customerId);

        Customer createdCustomer = this.customerRepositoryService.saveCustomer(newCustomer);

        return getCreateCustomerResponse(createdCustomer);
    }

    private boolean customerAlreadyExists(String customerId) {
        return this.customerRepositoryService.getCustomerById(customerId).isPresent();
    }

    private String getCustomerId(Document document) {
        return MessageFormat.format(CUSTOMER_ID_TEMPLATE, document.type(), document.number());
    }

    private CreateCustomerResponse getCreateCustomerResponse(Customer createdCustomer) {
        return new CreateCustomerResponse(createdCustomer.getId(),
                createdCustomer.getName(),
                createdCustomer.getSurname(),
                createdCustomer.getDocument(),
                createdCustomer.getCreatedAt().toString(),
                createdCustomer.getCreatedBy());
    }

    private Customer getNewCustomer(CreateCustomerRequest createCustomerRequest, String customerId) {
        Instant now = Instant.now();
        return new Customer(
                customerId,
                createCustomerRequest.name(),
                createCustomerRequest.surname(),
                createCustomerRequest.document(),
                now,
                createCustomerRequest.currentUser(),
                now,
                createCustomerRequest.currentUser(),
                null,
                CustomerState.CREATED
        );
    }

}
