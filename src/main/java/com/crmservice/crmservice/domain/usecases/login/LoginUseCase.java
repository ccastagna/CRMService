package com.crmservice.crmservice.domain.usecases.login;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.interfaces.ITokenService;
import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.responses.DomainErrorResponse;
import com.crmservice.crmservice.domain.services.authentication.IAuthenticationService;
import com.crmservice.crmservice.infrastructure.drivers.interfaces.ILoginUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LoginUseCase implements ILoginUseCase {

    private final Logger logger = LoggerFactory.getLogger(LoginUseCase.class);
    private static final String SUCCESSFUL_LOGIN_MESSAGE_TEMPLATE = "Login successful of user: {}.";

    private IUserRepositoryService userRepositoryService;
    private IAuthenticationService authenticationService;
    private ITokenService tokenService;

    public LoginUseCase(IUserRepositoryService userRepositoryService,
                        IAuthenticationService authenticationService,
                        ITokenService tokenService) {
        this.userRepositoryService = userRepositoryService;
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
    }

    public LoginResponse login(LoginRequest loginRequest) throws DomainClientException {

        User user = this.userRepositoryService.getUserByUsername(loginRequest.getRequestUsername())
                .orElseThrow(new DomainClientException(DomainErrorResponse.USERNAME_DOES_NOT_MATCH));

        checkUserIsAvailable(user);

        this.authenticationService.authenticateCredentials(user, loginRequest);

        String token = this.tokenService.create(
                Map.of(
                        "userId", user.getId(),
                        "ip", loginRequest.getIp(),
                        "role", user.getRole()
                ));

        logger.info(SUCCESSFUL_LOGIN_MESSAGE_TEMPLATE, loginRequest.getRequestUsername());

        return new LoginResponse(token);
    }

    private void checkUserIsAvailable(User user) throws DomainClientException {
        if (this.authenticationService.isLoginLocked(user)) {
            throw new DomainClientException(DomainErrorResponse.TOO_MANY_LOGIN_ATTEMPTS);
        }
    }
}
