package com.crmservice.crmservice.dependencyinjectors;

import com.crmservice.crmservice.domain.interfaces.ICounterRepositoryService;
import com.crmservice.crmservice.domain.interfaces.ITokenService;
import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import com.crmservice.crmservice.domain.services.attemptscounter.AttemptsCounter;
import com.crmservice.crmservice.domain.services.attemptscounter.IAttemptsCounter;
import com.crmservice.crmservice.domain.services.authentication.AuthenticationService;
import com.crmservice.crmservice.domain.services.authentication.IAuthenticationService;
import com.crmservice.crmservice.domain.services.credentialsvalidators.IUserCredentialsValidator;
import com.crmservice.crmservice.domain.services.credentialsvalidators.UserCredentialsValidator;
import com.crmservice.crmservice.domain.usecases.createuser.CreateUserUseCase;
import com.crmservice.crmservice.domain.usecases.login.LoginUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.ICreateUserUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.ILoginUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Order(2)
public class DomainConfiguration {

    @Bean
    IUserCredentialsValidator getUserCredentialsValidator(@Autowired IUserRepositoryService userRepositoryService,
                                                             @Autowired PasswordEncoder passwordEncoder) {
        return new UserCredentialsValidator(userRepositoryService, passwordEncoder);
    }

    @Bean
    IAttemptsCounter getAttemptsCounter(@Autowired ICounterRepositoryService counterRepositoryService) {
        return new AttemptsCounter(counterRepositoryService);
    }

    @Bean
    IAuthenticationService getAuthenticationService(@Autowired IUserCredentialsValidator userCredentialsValidator,
                                                    @Autowired IAttemptsCounter attemptsCounter) {
        return new AuthenticationService(userCredentialsValidator, attemptsCounter);
    }

    @Bean
    ILoginUseCase getLoginUseCase(@Autowired IUserRepositoryService userRepositoryService,
                                        @Autowired IAuthenticationService authenticationService,
                                        @Autowired ITokenService tokenService) {
        return new LoginUseCase(userRepositoryService, authenticationService, tokenService);
    }

    @Bean
    ICreateUserUseCase getCreateUserUseCase(@Autowired IUserCredentialsValidator userCredentialsValidator,
                                            @Autowired PasswordEncoder passwordEncoder,
                                            @Autowired IUserRepositoryService userRepositoryService) {
        return new CreateUserUseCase(userCredentialsValidator, passwordEncoder, userRepositoryService);
    }
}
