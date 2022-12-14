package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.deleteuser.DeleteUserRequest;
import com.crmservice.crmservice.domain.usecases.interfaces.IDeleteUserUseCase;
import com.crmservice.crmservice.infrastructure.drivers.logs.LogMessageBuilder;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class DeleteUserRequestHandler extends BaseRequestHandler<Void, Void> {

    private static final String ROOT_USERNAME = "root";
    private final IDeleteUserUseCase deleteUserUseCase;

    private final Logger logger = LoggerFactory.getLogger(DeleteUserRequestHandler.class);

    public DeleteUserRequestHandler(IDeleteUserUseCase deleteUserUseCase) {
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @Override
    public ResponseEntity<Void> handle(RequestDTO<Void> request) {

        ResponseEntity response;

        try {
            String usernameToDelete = Optional.ofNullable(request.getContext(RequestContextKey.USERNAME_TO_DELETE))
                    .orElseThrow();
            String currentUserUsername = Optional.ofNullable(request.getContext(RequestContextKey.CURRENT_USER))
                    .orElseThrow();

            if (usernameToDelete.equals(currentUserUsername) || usernameToDelete.equals(ROOT_USERNAME)) {
                return HttpAdapterResponseBuilder.forbidden("It is forbidden to delete the given user");
            }

            DeleteUserRequest deleteUserRequest = new DeleteUserRequest(usernameToDelete);

            this.deleteUserUseCase.deleteUser(deleteUserRequest);

            response = HttpAdapterResponseBuilder.noContent();

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
