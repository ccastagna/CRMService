package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.usecases.interfaces.IGetAllCustomersUseCase;
import com.crmservice.crmservice.infrastructure.drivers.logs.LogMessageBuilder;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import com.crmservice.crmservice.infrastructure.drivers.responses.dtos.GetAllCustomersResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class GetAllCustomersRequestHandler extends BaseRequestHandler<Void, GetAllCustomersResponseDTO> {

    private final IGetAllCustomersUseCase getAllCustomersUseCase;

    private final Logger logger = LoggerFactory.getLogger(GetAllCustomersRequestHandler.class);

    public GetAllCustomersRequestHandler(IGetAllCustomersUseCase getAllCustomersUseCase) {
        this.getAllCustomersUseCase = getAllCustomersUseCase;
    }

    @Override
    public ResponseEntity<GetAllCustomersResponseDTO> handle(RequestDTO<Void> request) {

        ResponseEntity response;

        try {

            List<Customer> getAllCustomersResponse = this.getAllCustomersUseCase.getAllCustomers();

            response = ResponseEntity.ok()
                    .body(GetAllCustomersResponseDTO.from(getAllCustomersResponse));

        } catch (Exception ex) {
            response = HttpAdapterResponseBuilder.internalServerError();
            logger.error(LogMessageBuilder.build(request.getRequestEntity(), ex.getMessage(), getStackTrace(ex.getStackTrace())));
        }

        return response;
    }

}
