package com.crmservice.crmservice.domain.usecases.interfaces;

import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.updateuser.UpdateUserRequest;
import com.crmservice.crmservice.domain.usecases.updateuser.UpdateUserResponse;

public interface IUpdateUserUseCase {
    UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest) throws DomainClientException;
}
