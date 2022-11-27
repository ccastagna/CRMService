package com.crmservice.crmservice.domain.services.authentication;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.responses.DomainErrorResponse;
import com.crmservice.crmservice.domain.services.attemptscounter.IAttemptsCounter;
import com.crmservice.crmservice.domain.services.credentialsvalidators.IUserCredentialsValidator;
import com.crmservice.crmservice.domain.usecases.login.LoginRequest;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

import java.util.Optional;
import java.util.UUID;

public class AuthenticationService implements IAuthenticationService {

    private static final String INVALID_CREDENTIALS_EVENT = "invalid-credentials";
    private static final Integer INVALID_PASSWORD_EVENT_COUNTER_TTL_IN_SECONDS = 300;
    private static final Integer MAX_LOGIN_ATTEMPTS = 3;

    private IUserCredentialsValidator userCredentialsValidator;
    private IAttemptsCounter attemptsCounter;

    public AuthenticationService(IUserCredentialsValidator userCredentialsValidator,
                                 IAttemptsCounter attemptsCounter) {
        this.userCredentialsValidator = userCredentialsValidator;
        this.attemptsCounter = attemptsCounter;
    }

    @Override
    public void authenticateCredentials(User user, LoginRequest loginRequest) throws DomainClientException {

        if (!this.userCredentialsValidator.isRequestUsernameValid(user.getUsername(), loginRequest.getRequestUsername())) {
            incrementCredentialsErrorCounter(user.getId());
            throw new DomainClientException(DomainErrorResponse.MALFORMED_USERNAME);
        }

        if (!this.userCredentialsValidator.isRequestUserPasswordValid(user.getPassword(), loginRequest.getRequestPassword())) {
            incrementCredentialsErrorCounter(user.getId());
            throw new DomainClientException(DomainErrorResponse.MALFORMED_PASSWORD);
        }

        this.attemptsCounter.reset(
                user.getId().toString(),
                INVALID_CREDENTIALS_EVENT,
                INVALID_PASSWORD_EVENT_COUNTER_TTL_IN_SECONDS
        );
    }

    public Boolean isLoginLocked(User user) {
        Optional<Integer> invalidAttempts = this.attemptsCounter.get(user.getId().toString(), INVALID_CREDENTIALS_EVENT);
        return invalidAttempts
                .filter(value -> value >= MAX_LOGIN_ATTEMPTS)
                .map(value -> true)
                .orElse(false);
    }

    private void incrementCredentialsErrorCounter(UUID userId) throws DomainClientException {
        Integer invalidPasswordTimes = attemptsCounter.count(userId.toString(),
                INVALID_CREDENTIALS_EVENT,
                INVALID_PASSWORD_EVENT_COUNTER_TTL_IN_SECONDS);
        if (invalidPasswordTimes >= MAX_LOGIN_ATTEMPTS) {
            throw new DomainClientException(DomainErrorResponse.TOO_MANY_LOGIN_ATTEMPTS);
        }
    }
}
