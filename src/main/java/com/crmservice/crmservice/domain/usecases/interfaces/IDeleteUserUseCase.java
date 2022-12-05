package com.crmservice.crmservice.domain.usecases.interfaces;

import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.deleteuser.DeleteUserRequest;

public interface IDeleteUserUseCase {
    void deleteUser(DeleteUserRequest deleteUserRequest) throws DomainClientException;
}
