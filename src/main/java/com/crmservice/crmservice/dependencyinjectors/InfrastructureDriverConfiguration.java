package com.crmservice.crmservice.dependencyinjectors;


import com.crmservice.crmservice.infrastructure.drivers.interfaces.ILoginUseCase;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.BasicAuthorizationHeaderHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.IPHeaderHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.IRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.requesthandlers.LoginRequestHandler;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

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
    @Qualifier("BasicAuthHeaderHandler")
    IRequestHandler<Void, Void> getBasicAuthorizationHeaderHandler() {
        return new BasicAuthorizationHeaderHandler();
    }

    @Bean
    @Qualifier("IPHeaderHandler")
    IRequestHandler<Void, Void> getIPHeaderHandler() {
        return new IPHeaderHandler();
    }


}
