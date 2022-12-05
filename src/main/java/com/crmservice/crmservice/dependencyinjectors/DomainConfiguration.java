package com.crmservice.crmservice.dependencyinjectors;

import com.crmservice.crmservice.domain.interfaces.ICounterRepositoryService;
import com.crmservice.crmservice.domain.interfaces.ICustomerRepositoryService;
import com.crmservice.crmservice.domain.interfaces.ITokenService;
import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import com.crmservice.crmservice.domain.services.attemptscounter.AttemptsCounter;
import com.crmservice.crmservice.domain.services.attemptscounter.IAttemptsCounter;
import com.crmservice.crmservice.domain.services.authentication.AuthenticationService;
import com.crmservice.crmservice.domain.services.authentication.IAuthenticationService;
import com.crmservice.crmservice.domain.services.credentialsvalidators.IUserCredentialsValidator;
import com.crmservice.crmservice.domain.services.credentialsvalidators.UserCredentialsValidator;
import com.crmservice.crmservice.domain.usecases.createcustomer.CreateCustomerUseCase;
import com.crmservice.crmservice.domain.usecases.createuser.CreateUserUseCase;
import com.crmservice.crmservice.domain.usecases.deletecustomer.DeleteCustomerUseCase;
import com.crmservice.crmservice.domain.usecases.deleteuser.DeleteUserUseCase;
import com.crmservice.crmservice.domain.usecases.getallusers.GetAllUsersUseCase;
import com.crmservice.crmservice.domain.usecases.getcustomer.GetCustomerUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.ICreateCustomerUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.IDeleteCustomerUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.IDeleteUserUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.IGetAllUsersUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.IGetCustomerUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.IUpdateCustomerUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.IUpdateUserUseCase;
import com.crmservice.crmservice.domain.usecases.login.LoginUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.ICreateUserUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.ILoginUseCase;
import com.crmservice.crmservice.domain.usecases.updatecustomer.UpdateCustomerUseCase;
import com.crmservice.crmservice.domain.usecases.updateuser.UpdateUserUseCase;
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

    @Bean
    IUpdateUserUseCase getUpdateUserUseCase(@Autowired IUserCredentialsValidator userCredentialsValidator,
                                            @Autowired PasswordEncoder passwordEncoder,
                                            @Autowired IUserRepositoryService userRepositoryService) {
        return new UpdateUserUseCase(userCredentialsValidator, passwordEncoder, userRepositoryService);
    }

    @Bean
    IDeleteUserUseCase getDeleteUserUseCase(@Autowired IUserRepositoryService userRepositoryService) {
        return new DeleteUserUseCase(userRepositoryService);
    }

    @Bean
    IGetAllUsersUseCase getGetAllUsersUseCase(@Autowired IUserRepositoryService userRepositoryService) {
        return new GetAllUsersUseCase(userRepositoryService);
    }

    @Bean
    ICreateCustomerUseCase getCreateCustomerUseCase(@Autowired ICustomerRepositoryService customerRepositoryService) {
        return new CreateCustomerUseCase(customerRepositoryService);
    }

    @Bean
    IUpdateCustomerUseCase getUpdateCustomerUseCase(@Autowired ICustomerRepositoryService customerRepositoryService) {
        return new UpdateCustomerUseCase(customerRepositoryService);
    }

    @Bean
    IDeleteCustomerUseCase getDeleteCustomerUseCase(@Autowired ICustomerRepositoryService customerRepositoryService) {
        return new DeleteCustomerUseCase(customerRepositoryService);
    }

    @Bean
    IGetCustomerUseCase getGetCustomerUseCase(@Autowired ICustomerRepositoryService customerRepositoryService) {
        return new GetCustomerUseCase(customerRepositoryService);
    }
}
