package com.crmservice.crmservice.domain.services.authentication;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.login.LoginRequest;

public interface IAuthenticationService {
    void authenticateCredentials(User user, LoginRequest loginRequest) throws DomainClientException;
    Boolean isLoginLocked(User user);
}
