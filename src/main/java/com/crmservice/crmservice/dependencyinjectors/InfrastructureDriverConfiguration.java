package com.crmservice.crmservice.dependencyinjectors;


import com.crmservice.crmservice.domain.interfaces.ITokenService;
import com.crmservice.crmservice.domain.usecases.interfaces.ICreateCustomerUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.ICreateUserUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.IDeleteCustomerUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.IDeleteUserUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.IGetAllUsersUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.IGetCustomerUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.ILoginUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.IUpdateCustomerUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.IUpdateUserUseCase;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.BasicAuthorizationHeaderHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.BearerAuthorizationHeaderHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.CreateCustomerRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.CreateUserRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.DeleteCustomerRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.DeleteUserRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.GetAllUsersRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.GetCustomerRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.IPHeaderHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.IRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.LoginRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.UpdateCustomerRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.UpdateUserRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.CreateCustomerRequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.CreateUserRequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.UpdateCustomerRequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.UpdateUserRequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.CreateCustomerResponseDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.CreateUserResponseDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.GetAllUsersResponseDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.GetCustomerResponseDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.LoginResponseDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.UpdateCustomerResponseDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.UpdateUserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(3)
public class InfrastructureDriverConfiguration {

    @Bean
    IRequestHandler<Void, LoginResponseDTO> getLoginRequestHandler(
            @Autowired ILoginUseCase loginUseCase
    ) {
        return new LoginRequestHandler(loginUseCase);
    }

    @Bean
    IRequestHandler<CreateUserRequestDTO, CreateUserResponseDTO> getCreateUserRequestHandler(
            @Autowired ICreateUserUseCase createUserUseCase
    ) {
        return new CreateUserRequestHandler(createUserUseCase);
    }

    @Bean
    IRequestHandler<UpdateUserRequestDTO, UpdateUserResponseDTO> getUpdateUserRequestHandler(
            @Autowired IUpdateUserUseCase updateUserUseCase
    ) {
        return new UpdateUserRequestHandler(updateUserUseCase);
    }

    @Bean
    @Qualifier("DeleteUserRequestHandler")
    IRequestHandler<Void, Void> getDeleteUserRequestHandler(
            @Autowired IDeleteUserUseCase deleteUserUseCase
    ) {
        return new DeleteUserRequestHandler(deleteUserUseCase);
    }

    @Bean
    IRequestHandler<Void, GetAllUsersResponseDTO> getGetAllUsersRequestHandler(
            @Autowired IGetAllUsersUseCase getAllUsersUseCase
    ) {
        return new GetAllUsersRequestHandler(getAllUsersUseCase);
    }

    @Bean
    IRequestHandler<CreateCustomerRequestDTO, CreateCustomerResponseDTO> getCreateCustomerRequestHandler(
            @Autowired ICreateCustomerUseCase createCustomerUseCase
    ) {
        return new CreateCustomerRequestHandler(createCustomerUseCase);
    }

    @Bean
    IRequestHandler<UpdateCustomerRequestDTO, UpdateCustomerResponseDTO> getUpdateCustomerRequestHandler(
            @Autowired IUpdateCustomerUseCase updateCustomerUseCase
    ) {
        return new UpdateCustomerRequestHandler(updateCustomerUseCase);
    }


    @Bean
    @Qualifier("DeleteCustomerRequestHandler")
    IRequestHandler<Void, Void> getDeleteCustomerRequestHandler(
            @Autowired IDeleteCustomerUseCase deleteCustomerUseCase
    ) {
        return new DeleteCustomerRequestHandler(deleteCustomerUseCase);
    }


    @Bean
    IRequestHandler<Void, GetCustomerResponseDTO> getGetCustomerRequestHandler(
            @Autowired IGetCustomerUseCase getCustomerUseCase
    ) {
        return new GetCustomerRequestHandler(getCustomerUseCase);
    }

    @Bean
    @Qualifier("BasicAuthHeaderHandler")
    IRequestHandler<Void, Void> getBasicAuthorizationHeaderHandler() {
        return new BasicAuthorizationHeaderHandler();
    }

    @Bean
    @Qualifier("BearerAuthHeaderHandler")
    IRequestHandler<Void, Void> getBearerAuthorizationHeaderHandler(@Autowired ITokenService tokenService) {
        return new BearerAuthorizationHeaderHandler(tokenService);
    }

    @Bean
    @Qualifier("IPHeaderHandler")
    IRequestHandler<Void, Void> getIPHeaderHandler() {
        return new IPHeaderHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll().and()
                .csrf().disable();
        return http.build();
    }

}
