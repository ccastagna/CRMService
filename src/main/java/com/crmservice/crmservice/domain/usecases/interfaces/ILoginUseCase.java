package com.crmservice.crmservice.domain.usecases.interfaces;

import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.login.LoginRequest;
import com.crmservice.crmservice.domain.usecases.login.LoginResponse;

public interface ILoginUseCase {
    LoginResponse login(LoginRequest loginRequest) throws DomainClientException;
}
