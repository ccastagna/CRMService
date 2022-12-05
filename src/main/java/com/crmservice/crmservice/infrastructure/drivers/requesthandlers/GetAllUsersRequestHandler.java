package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.interfaces.IGetAllUsersUseCase;
import com.crmservice.crmservice.infrastructure.drivers.logs.LogMessageBuilder;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.GetAllUsersResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class GetAllUsersRequestHandler extends BaseRequestHandler<Void, GetAllUsersResponseDTO> {

    private final IGetAllUsersUseCase getAllUsersUseCase;

    private final Logger logger = LoggerFactory.getLogger(GetAllUsersRequestHandler.class);

    public GetAllUsersRequestHandler(IGetAllUsersUseCase getAllUsersUseCase) {
        this.getAllUsersUseCase = getAllUsersUseCase;
    }

    @Override
    public ResponseEntity<GetAllUsersResponseDTO> handle(RequestDTO<Void> request) {

        ResponseEntity response;

        try {

            List<User> getAllUsersResponse = this.getAllUsersUseCase.getAllUsers();

            response = ResponseEntity.ok()
                    .body(GetAllUsersResponseDTO.from(getAllUsersResponse));

        } catch (Exception ex) {
            response = HttpAdapterResponseBuilder.internalServerError();
            logger.error(LogMessageBuilder.build(request.getRequestEntity(), ex.getMessage(), getStackTrace(ex.getStackTrace())));
        }

        return response;
    }

}
