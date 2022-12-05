package com.crmservice.crmservice.domain.usecases.interfaces;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.usecases.updatecustomer.UpdateCustomerRequest;

public interface IUpdateCustomerUseCase {
    Customer updateCustomer(UpdateCustomerRequest updateCustomerRequest);
}
