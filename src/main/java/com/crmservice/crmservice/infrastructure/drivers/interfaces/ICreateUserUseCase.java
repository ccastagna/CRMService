package com.crmservice.crmservice.infrastructure.drivers.interfaces;

import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.createuser.CreateUserRequest;
import com.crmservice.crmservice.domain.usecases.createuser.CreateUserResponse;

public interface ICreateUserUseCase {
    CreateUserResponse createUser(CreateUserRequest createUserRequest) throws DomainClientException;
}
