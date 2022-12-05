package com.crmservice.crmservice.domain.usecases.interfaces;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.updatecustomerphoto.UpdateCustomerPhotoRequest;

import java.io.IOException;

public interface IUpdateCustomerPhotoUseCase {
    Customer updateCustomerPhoto(UpdateCustomerPhotoRequest updateCustomerPhotoRequest) throws DomainClientException, IOException;
}
