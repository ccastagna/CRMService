package com.crmservice.crmservice.domain.services.authentication;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.enums.Role;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.responses.DomainErrorResponse;
import com.crmservice.crmservice.domain.services.attemptscounter.IAttemptsCounter;
import com.crmservice.crmservice.domain.services.credentialsvalidators.IUserCredentialsValidator;
import com.crmservice.crmservice.domain.usecases.login.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.MessageFormat;
import java.util.Base64;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthenticationServiceTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USER_IP = "127.0.0.0";
    private static final String USERNAME = "username";
    private static final String ENCODED_PASSWORD = "userEncodedPassword";
    private static final String NAME_PASSWORD = MessageFormat.format("{0}:{1}",
            USERNAME,
            ENCODED_PASSWORD);
    private static final String AUTHENTICATION_TOKEN = Base64.getEncoder().encodeToString(NAME_PASSWORD.getBytes());
    private static final String INVALID_CREDENTIALS_EVENT = "invalid-credentials";

    private User user;
    private LoginRequest loginRequest;

    private IAuthenticationService authenticationService;

    @Mock
    private IUserCredentialsValidator userCredentialsValidator;
    @Mock
    private IAttemptsCounter attemptsCounter;


    @BeforeEach
    void setUp() throws DomainClientException {
        this.authenticationService = new AuthenticationService(userCredentialsValidator, attemptsCounter);
        this.user = new User(USER_ID, USERNAME, ENCODED_PASSWORD, Role.ROOT);
        this.loginRequest = new LoginRequest(AUTHENTICATION_TOKEN, USER_IP);
    }

    @Test
    void givenValidUsernameAndValidPassword_whenAuthenticate_thenDoNothing() throws DomainClientException {

        givenValidUsername();
        givenValidPassword();

        this.authenticationService.authenticateCredentials(this.user, this.loginRequest);

        verify(this.userCredentialsValidator)
                .isRequestUsernameValid(this.user.getUsername(), this.loginRequest.getRequestUsername());
        verify(this.userCredentialsValidator)
                .isRequestUserPasswordValid(this.user.getPassword(), this.loginRequest.getRequestPassword());
        verify(this.attemptsCounter, never()).count(any(), any(), any());
    }

    @Test
    void givenInvalidUsername_whenAuthenticate_thenThrowDomainClientExceptionDueToMalformedName() {
        givenInvalidUsername();

        assertThatExceptionOfType(DomainClientException.class)
                .isThrownBy(() -> this.authenticationService.authenticateCredentials(this.user, this.loginRequest))
                .withMessage(DomainErrorResponse.MALFORMED_USERNAME.getMessage());

        verify(this.userCredentialsValidator)
                .isRequestUsernameValid(this.user.getUsername(), this.loginRequest.getRequestUsername());
        verify(this.userCredentialsValidator, never())
                .isRequestUserPasswordValid(this.user.getPassword(), this.loginRequest.getRequestPassword());
        verify(this.attemptsCounter).count(eq(USER_ID.toString()), eq(INVALID_CREDENTIALS_EVENT), any());
    }

    @Test
    void givenInvalidPasswordForLessThanThreeTimes_whenAuthenticate_thenThrowDomainClientExceptionDueToMalformedPassword() {
        givenValidUsername();
        givenInvalidPassword();
        givenInvalidUserPasswordCountEqualsToTwo();

        assertThatExceptionOfType(DomainClientException.class)
                .isThrownBy(() -> this.authenticationService.authenticateCredentials(this.user, this.loginRequest))
                .withMessage(DomainErrorResponse.MALFORMED_PASSWORD.getMessage());

        verify(this.userCredentialsValidator)
                .isRequestUsernameValid(this.user.getUsername(), this.loginRequest.getRequestUsername());
        verify(this.userCredentialsValidator)
                .isRequestUserPasswordValid(this.user.getPassword(), this.loginRequest.getRequestPassword());
        verify(this.attemptsCounter).count(eq(USER_ID.toString()), eq(INVALID_CREDENTIALS_EVENT), any());
    }

    @Test
    void givenInvalidPasswordForAtLeastThreeTimes_whenAuthenticate_thenThrowDomainClientExceptionDueToTooManyLoginAttempts() {
        givenValidUsername();
        givenInvalidPassword();
        givenInvalidUserCredentialsCountEqualsToThree();

        assertThatExceptionOfType(DomainClientException.class)
                .isThrownBy(() -> this.authenticationService.authenticateCredentials(this.user, this.loginRequest))
                .withMessage(DomainErrorResponse.TOO_MANY_LOGIN_ATTEMPTS.getMessage());

        verify(this.userCredentialsValidator)
                .isRequestUsernameValid(this.user.getUsername(), this.loginRequest.getRequestUsername());
        verify(this.userCredentialsValidator)
                .isRequestUserPasswordValid(this.user.getPassword(), this.loginRequest.getRequestPassword());
        verify(this.attemptsCounter).count(eq(USER_ID.toString()), eq(INVALID_CREDENTIALS_EVENT), any());
    }

    private void givenInvalidUserCredentialsCountEqualsToThree() {
        when(this.attemptsCounter.count(eq(USER_ID.toString()), eq(INVALID_CREDENTIALS_EVENT), any())).thenReturn(3);
    }

    private void givenInvalidUserPasswordCountEqualsToTwo() {
        when(this.attemptsCounter.count(eq(USER_ID.toString()), eq(INVALID_CREDENTIALS_EVENT), any())).thenReturn(2);
    }

    private void givenValidPassword() {
        when(this.userCredentialsValidator.isRequestUserPasswordValid(
                this.user.getPassword(),
                this.loginRequest.getRequestPassword()))
                .thenReturn(true);
    }

    private void givenValidUsername() {
        when(this.userCredentialsValidator.isRequestUsernameValid(
                this.user.getUsername(),
                this.loginRequest.getRequestUsername()))
                .thenReturn(true);
    }

    private void givenInvalidPassword() {
        when(this.userCredentialsValidator.isRequestUserPasswordValid(
                this.user.getPassword(),
                this.loginRequest.getRequestPassword()))
                .thenReturn(false);
    }

    private void givenInvalidUsername() {
        when(this.userCredentialsValidator.isRequestUsernameValid(
                this.user.getUsername(),
                this.loginRequest.getRequestUsername()))
                .thenReturn(false);
    }
}