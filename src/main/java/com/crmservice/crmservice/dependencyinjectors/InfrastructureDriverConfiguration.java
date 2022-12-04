package com.crmservice.crmservice.dependencyinjectors;


import com.crmservice.crmservice.domain.interfaces.ITokenService;
import com.crmservice.crmservice.domain.usecases.interfaces.ICreateUserUseCase;
import com.crmservice.crmservice.domain.usecases.interfaces.ILoginUseCase;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.BasicAuthorizationHeaderHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.BearerAuthorizationHeaderHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.CreateUserRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.IPHeaderHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.IRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.LoginRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.CreateUserRequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.CreateUserResponseDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.LoginResponseDTO;
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
