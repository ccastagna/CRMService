package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.domain.enums.Role;
import com.crmservice.crmservice.domain.enums.UseCase;
import com.crmservice.crmservice.domain.interfaces.ITokenService;
import com.crmservice.crmservice.infrastructure.drivers.logs.LogMessageBuilder;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import dev.paseto.jpaseto.Paseto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.CURRENT_USER;
import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.REQUEST_IP;
import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.USE_CASE;

public class BearerAuthorizationHeaderHandler<T, R> extends BaseRequestHandler<T, R> {

    private final Logger logger = LoggerFactory.getLogger(BearerAuthorizationHeaderHandler.class);

    private static final String HEADER_NAME = "Authorization";
    private static final String BEARER_AUTHORIZATION_TYPE = "Bearer";
    private final ITokenService tokenService;

    public BearerAuthorizationHeaderHandler(ITokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public ResponseEntity handle(RequestDTO<T> request) {

        ResponseEntity response;

        try {
            Paseto paseto = parsePasetoToken(request);

            validateUserPermissions(request, paseto);

            String currentUserUsername = getCurrentUserUsernameFrom(paseto);

            request.setContext(CURRENT_USER, currentUserUsername);

            response = super.handle(request);

        } catch (SecurityException ex) {
            response = HttpAdapterResponseBuilder.unauthorizedToken();
            logger.error(LogMessageBuilder.build(request.getRequestEntity(), ex.getMessage(), getStackTrace(ex.getStackTrace())));

        } catch (Exception ex) {
            response = HttpAdapterResponseBuilder.internalServerError();
            logger.error(LogMessageBuilder.build(request.getRequestEntity(), ex.getMessage(), getStackTrace(ex.getStackTrace())));
        }

        return response;
    }

    private String getCurrentUserUsernameFrom(Paseto paseto) {
        return Optional.of(paseto.getClaims().get("username", String.class)).orElseThrow();
    }

    private void validateUserPermissions(RequestDTO<T> request, Paseto paseto) {
        Role userRole = getRole(paseto);
        UseCase currentUseCase = getCurrentUseCase(request);
        validatePermissions(userRole, currentUseCase);
    }

    private Paseto parsePasetoToken(RequestDTO<T> request) {
        String authorizationToken = parseBearerAuthorizationHeader(getAuthorizationHeader(request));

        Map<String, String> claimsRequired = Map.of("ip", request.getContext(REQUEST_IP));
        Paseto paseto = this.tokenService.validate(authorizationToken, claimsRequired);
        return paseto;
    }

    private UseCase getCurrentUseCase(RequestDTO<T> request) {
        return Optional.of(request.getContext(USE_CASE))
                .map(UseCase::valueOf)
                .orElseThrow();
    }

    private Role getRole(Paseto paseto) {
        return Optional.of(paseto.getClaims().get("role", String.class))
                .map(Role::valueOf)
                .orElseThrow();
    }

    private void validatePermissions(Role role, UseCase useCase) {
        if (!role.hasPermissionsTo(useCase)) {
            String message = MessageFormat.format("Role {0} does not have permissions to {1}", role, useCase);
            throw new SecurityException(message);
        }
    }

    private List<String> getAuthorizationHeader(RequestDTO<T> request) {
        return Optional.ofNullable(request.getRequestEntity())
                .map(requestEntity -> requestEntity.getHeaders().get(HEADER_NAME))
                .orElseGet(() -> List.of(
                        request.getHttpServletRequest().getHeader(HEADER_NAME)
                ));
    }

    private String parseBearerAuthorizationHeader(List<String> authorizationHeader) {
        String[] parsedAuthorizationHeader = Optional.of(authorizationHeader)
                .map(Collection::stream)
                .flatMap(Stream::findFirst)
                .map(value -> value.split("\s"))
                .orElseThrow(SecurityException::new);

        if (!BEARER_AUTHORIZATION_TYPE.equals(parsedAuthorizationHeader[0])) {
            throw new SecurityException("Incorrect authorization type.");
        }

        return parsedAuthorizationHeader[1];
    }
}
