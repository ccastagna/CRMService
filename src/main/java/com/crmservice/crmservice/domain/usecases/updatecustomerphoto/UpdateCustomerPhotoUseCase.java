package com.crmservice.crmservice.domain.usecases.updatecustomerphoto;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.enums.CustomerState;
import com.crmservice.crmservice.domain.interfaces.ICustomerRepositoryService;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.interfaces.IUpdateCustomerPhotoUseCase;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;

import static com.crmservice.crmservice.domain.responses.DomainErrorResponse.CUSTOMER_DOES_NOT_EXIST;

public class UpdateCustomerPhotoUseCase implements IUpdateCustomerPhotoUseCase {

    private final ICustomerRepositoryService customerRepositoryService;

    public UpdateCustomerPhotoUseCase(ICustomerRepositoryService customerRepositoryService) {
        this.customerRepositoryService = customerRepositoryService;
    }

    @Override
    public Customer updateCustomerPhoto(UpdateCustomerPhotoRequest updateCustomerPhotoRequest) throws DomainClientException, IOException {

        String customerId = updateCustomerPhotoRequest.customerId();
        String currentUser = updateCustomerPhotoRequest.currentUser();
        MultipartFile photo = updateCustomerPhotoRequest.photo();

        Customer customerToUpdate = this.customerRepositoryService.getActiveCustomerById(customerId)
                .orElseThrow(new DomainClientException(CUSTOMER_DOES_NOT_EXIST));

        String photoUrl = this.customerRepositoryService.saveCustomerPhoto(photo);

        Customer updatedCustomer = getUpdatedCustomer(customerId, currentUser, photoUrl, customerToUpdate);

        return this.customerRepositoryService.saveCustomer(updatedCustomer);
    }

    private Customer getUpdatedCustomer(String customerId, String currentUser, String photoUrl, Customer customerToUpdate) {
        return new Customer(
                customerId,
                customerToUpdate.getName(),
                customerToUpdate.getSurname(),
                customerToUpdate.getDocument(),
                customerToUpdate.getCreatedAt(),
                customerToUpdate.getCreatedBy(),
                Instant.now(),
                currentUser,
                photoUrl,
                CustomerState.UPDATED);
    }

}
