package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.createuser.CreateUserRequest;
import com.crmservice.crmservice.domain.usecases.createuser.CreateUserResponse;
import com.crmservice.crmservice.infrastructure.drivers.interfaces.ICreateUserUseCase;
import com.crmservice.crmservice.infrastructure.drivers.logs.LogMessageBuilder;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.CreateUserRequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.CreateUserResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class CreateUserRequestHandler extends BaseRequestHandler<CreateUserRequestDTO, CreateUserResponseDTO> {

    private final ICreateUserUseCase createUserUseCase;

    private final Logger logger = LoggerFactory.getLogger(CreateUserRequestHandler.class);

    public CreateUserRequestHandler(ICreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @Override
    public ResponseEntity<CreateUserResponseDTO> handle(RequestDTO<CreateUserRequestDTO> request) {

        ResponseEntity response;

        try {
            CreateUserRequest createUserRequest = Optional.ofNullable(request.getRequestEntity().getBody())
                    .map(CreateUserRequestDTO::toCreateUserRequest)
                    .orElseThrow();

            CreateUserResponse createUserResponse = this.createUserUseCase.createUser(createUserRequest);

            response = ResponseEntity.ok()
                    .body(CreateUserResponseDTO.from(createUserResponse));

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
