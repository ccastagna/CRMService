package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.login.LoginRequest;
import com.crmservice.crmservice.domain.usecases.login.LoginResponse;
import com.crmservice.crmservice.domain.usecases.interfaces.ILoginUseCase;
import com.crmservice.crmservice.infrastructure.drivers.logs.LogMessageBuilder;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.LoginResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.REQUEST_IP;
import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.REQUEST_PASSWORD;
import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.REQUEST_USERNAME;

public class LoginRequestHandler extends BaseRequestHandler<Void, LoginResponseDTO> {

    private final ILoginUseCase loginUseCase;

    private final Logger logger = LoggerFactory.getLogger(LoginRequestHandler.class);

    public LoginRequestHandler(ILoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Override
    public ResponseEntity<LoginResponseDTO> handle(RequestDTO<Void> request) {

        ResponseEntity response;

        try {
            String requestUsername = request.getContext(REQUEST_USERNAME);
            String requestPassword = request.getContext(REQUEST_PASSWORD);
            String requestIP = request.getContext(REQUEST_IP);
            LoginRequest loginRequest = new LoginRequest(requestUsername, requestPassword, requestIP);
            LoginResponse loginResponse = this.loginUseCase.login(loginRequest);

            response = ResponseEntity.ok()
                    .body(LoginResponseDTO.from(loginResponse));

        } catch (SecurityException ex) {
            response = HttpAdapterResponseBuilder.malformedExpectedData();
            logger.error(LogMessageBuilder.build(request.getRequestEntity(), ex.getMessage(), getStackTrace(ex.getStackTrace())));

        } catch (DomainClientException ex) {
            response = HttpAdapterResponseBuilder.fromDomainException(ex);
            logger.error(LogMessageBuilder.build(request.getRequestEntity(), ex.getMessage(), getStackTrace(ex.getStackTrace())));

        } catch (Exception ex) {
            response = HttpAdapterResponseBuilder.internalServerError();
            logger.error(LogMessageBuilder.build(request.getRequestEntity(), ex.getMessage(), getStackTrace(ex.getStackTrace())));
        }

        return response;
    }

}
