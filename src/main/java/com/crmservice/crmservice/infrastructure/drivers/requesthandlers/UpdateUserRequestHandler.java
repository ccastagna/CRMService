package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.interfaces.IUpdateUserUseCase;
import com.crmservice.crmservice.domain.usecases.updateuser.UpdateUserRequest;
import com.crmservice.crmservice.domain.usecases.updateuser.UpdateUserResponse;
import com.crmservice.crmservice.infrastructure.drivers.logs.LogMessageBuilder;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.UpdateUserRequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.UpdateUserResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class UpdateUserRequestHandler extends BaseRequestHandler<UpdateUserRequestDTO, UpdateUserResponseDTO> {

    private final IUpdateUserUseCase updateUserUseCase;

    private final Logger logger = LoggerFactory.getLogger(UpdateUserRequestHandler.class);

    public UpdateUserRequestHandler(IUpdateUserUseCase updateUserUseCase) {
        this.updateUserUseCase = updateUserUseCase;
    }

    @Override
    public ResponseEntity<UpdateUserResponseDTO> handle(RequestDTO<UpdateUserRequestDTO> request) {

        ResponseEntity response;

        try {
            String usernameToUpdate = Optional.ofNullable(request.getContext(RequestContextKey.USERNAME_TO_UPDATE))
                    .orElseThrow();

            UpdateUserRequest updateUserRequest = Optional.ofNullable(request.getRequestEntity().getBody())
                    .map(createUserRequestDTO -> createUserRequestDTO.toUpdateUserRequest(usernameToUpdate))
                    .orElseThrow();

            UpdateUserResponse updateUserResponse = this.updateUserUseCase.updateUser(updateUserRequest);

            response = ResponseEntity.ok()
                    .body(UpdateUserResponseDTO.from(updateUserResponse));

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
