package com.crmservice.crmservice.domain.usecases.login;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.enums.UserState;
import com.crmservice.crmservice.domain.interfaces.ITokenService;
import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.responses.DomainErrorResponse;
import com.crmservice.crmservice.domain.services.authentication.IAuthenticationService;
import com.crmservice.crmservice.domain.usecases.interfaces.ILoginUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.crmservice.crmservice.domain.enums.Role.ROOT;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataMongoTest
class LoginUseCaseTest {
    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USER_IP = "127.0.0.0";
    private static final String USERNAME = "username";
    private static final String USER_ENCODED_PASSWORD = "userEncodedPassword";
    private static final String ACCESS_TOKEN = "accessToken";

    private ILoginUseCase loginUseCase;
    private LoginRequest loginRequest;
    private User user;
    @Mock
    private IUserRepositoryService userRepositoryService;
    @Mock
    private IAuthenticationService authenticationService;
    @Mock
    private ITokenService tokenService;

    @BeforeEach
    void setUp() {
        this.loginUseCase = new LoginUseCase(
                userRepositoryService,
                authenticationService,
                tokenService);

        this.loginRequest = new LoginRequest(USERNAME, USER_ENCODED_PASSWORD, USER_IP);
        this.user = new User(USER_ID, USERNAME, USER_ENCODED_PASSWORD, ROOT, UserState.ACTIVE);

        when(this.tokenService.create(Map.of(
                "username", USERNAME,
                "ip", USER_IP,
                "role", ROOT
        ))).thenReturn(ACCESS_TOKEN);
    }

    @Test
    void givenValidUsernameAndValidPassword_whenLogin_thenReturnLoginResponse() throws DomainClientException {
        givenExistentUser();
        givenValidCredentials();

        LoginResponse useCaseResponse = this.loginUseCase.login(this.loginRequest);

        Assertions.assertThat(useCaseResponse)
                .isNotNull()
                .matches(loginResponse ->
                        ACCESS_TOKEN.equals(loginResponse.token()), ACCESS_TOKEN);

        verify(userRepositoryService).getActiveUserByUsername(USERNAME);
        verify(authenticationService).authenticateCredentials(this.user, this.loginRequest);
        verify(tokenService).create(Map.of(
                "username", USERNAME,
                "ip", USER_IP,
                "role", ROOT));
    }

    @Test
    void givenInvalidUsername_whenLogin_thenThrowDomainClientExceptionDueToMalformedUsername() throws DomainClientException {
        givenExistentUser();
        givenInvalidUsername();

        assertThatExceptionOfType(DomainClientException.class)
                .isThrownBy(() -> this.loginUseCase.login(this.loginRequest))
                .withMessage(DomainErrorResponse.MALFORMED_USERNAME.getMessage());

        verify(userRepositoryService).getActiveUserByUsername(USERNAME);
        verify(authenticationService).authenticateCredentials(this.user, this.loginRequest);
        verify(tokenService, never()).create(any());
    }


    @Test
    void givenNonExistentUsername_whenLogin_thenThrowDomainClientExceptionDueToNonExistentRequestedUser() throws DomainClientException {
        givenNonExistentUser();

        assertThatExceptionOfType(DomainClientException.class)
                .isThrownBy(() -> this.loginUseCase.login(this.loginRequest))
                .withMessage(DomainErrorResponse.INVALID_USERNAME_CREDENTIAL.getMessage());

        verify(userRepositoryService).getActiveUserByUsername(USERNAME);
        verify(authenticationService, never()).authenticateCredentials(this.user, this.loginRequest);
        verify(tokenService, never()).create(any());
    }

    @Test
    void givenInvalidPasswordForLessThanThreeTimes_whenLogin_thenThrowDomainClientExceptionDueToMalformedPassword() throws DomainClientException {
        givenExistentUser();
        givenInvalidPassword();

        assertThatExceptionOfType(DomainClientException.class)
                .isThrownBy(() -> this.loginUseCase.login(this.loginRequest))
                .withMessage(DomainErrorResponse.MALFORMED_PASSWORD.getMessage());

        verify(userRepositoryService).getActiveUserByUsername(USERNAME);
        verify(authenticationService).authenticateCredentials(this.user, this.loginRequest);
        verify(tokenService, never()).create(any());
    }

    @Test
    void givenInvalidPasswordForAtLeastThreeTimes_whenLogin_thenThrowDomainClientExceptionDueToTooManyLoginAttempts() throws DomainClientException {
        givenExistentUser();
        givenLoginLockedByCurrentInvalidPassword();

        assertThatExceptionOfType(DomainClientException.class)
                .isThrownBy(() -> this.loginUseCase.login(this.loginRequest))
                .withMessage(DomainErrorResponse.TOO_MANY_LOGIN_ATTEMPTS.getMessage());

        verify(userRepositoryService).getActiveUserByUsername(USERNAME);
        verify(authenticationService).authenticateCredentials(this.user, this.loginRequest);
        verify(tokenService, never()).create(any());
    }


    @Test
    void givenValidPasswordAndLockedLogin_whenLogin_thenThrowDomainClientExceptionDueToTooManyLoginAttempts() throws DomainClientException {
        givenExistentUser();
        givenLoginAlreadyLocked();
        givenValidCredentials();

        assertThatExceptionOfType(DomainClientException.class)
                .isThrownBy(() -> this.loginUseCase.login(this.loginRequest))
                .withMessage(DomainErrorResponse.TOO_MANY_LOGIN_ATTEMPTS.getMessage());

        verify(userRepositoryService).getActiveUserByUsername(USERNAME);
        verify(authenticationService, never()).authenticateCredentials(this.user, this.loginRequest);
        verify(tokenService, never()).create(any());
    }

    private void givenLoginLockedByCurrentInvalidPassword() throws DomainClientException {
        doThrow(new DomainClientException(DomainErrorResponse.TOO_MANY_LOGIN_ATTEMPTS))
                .when(authenticationService)
                .authenticateCredentials(this.user, this.loginRequest);
    }

    private void givenLoginAlreadyLocked() throws DomainClientException {
        when(authenticationService.isLoginLocked(this.user)).thenReturn(true);
    }

    private void givenInvalidPassword() throws DomainClientException {
        doThrow(new DomainClientException(DomainErrorResponse.MALFORMED_PASSWORD))
                .when(authenticationService)
                .authenticateCredentials(this.user, this.loginRequest);
    }

    private void givenInvalidUsername() throws DomainClientException {
        doThrow(new DomainClientException(DomainErrorResponse.MALFORMED_USERNAME))
                .when(authenticationService)
                .authenticateCredentials(this.user, this.loginRequest);
    }

    private void givenValidCredentials() throws DomainClientException {
        doNothing().when(this.authenticationService).authenticateCredentials(this.user, this.loginRequest);
    }

    private void givenExistentUser() {
        when(this.userRepositoryService.getActiveUserByUsername(USERNAME)).thenReturn(Optional.of(this.user));
    }

    private void givenNonExistentUser() {
        when(this.userRepositoryService.getActiveUserByUsername(USERNAME)).thenReturn(Optional.empty());
    }

}