package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.infrastructure.drivers.logs.LogMessageBuilder;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Base64;
import java.util.List;

import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.REQUEST_PASSWORD;
import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.REQUEST_USERNAME;

public class BasicAuthorizationHeaderHandler<T, R> extends BaseRequestHandler<T, R> {

    private final Logger logger = LoggerFactory.getLogger(BasicAuthorizationHeaderHandler.class);
    private static final String BASIC_AUTHORIZATION_TYPE = "Basic";

    @Override
    public ResponseEntity<R> handle(RequestDTO<T> request) {

        ResponseEntity response;

        try {
            String authorizationToken = parseBasicAuthorizationHeader(getAuthorizationHeader(request));
            String[] credentials = extractCredentialsFromAuthenticationToken(authorizationToken);

            if (credentials.length != 2 || credentials[0].isBlank() || credentials[1].isBlank()) {
                String message = "The request username or password is blank.";
                logger.error(LogMessageBuilder.build(request.getRequestEntity(), message));
                return HttpAdapterResponseBuilder.malformedExpectedData();
            }

            request.setContext(REQUEST_USERNAME, credentials[0]);
            request.setContext(REQUEST_PASSWORD, credentials[1]);

            response = super.handle(request);

        } catch (SecurityException ex) {
            response = HttpAdapterResponseBuilder.malformedExpectedData();
            logger.error(LogMessageBuilder.build(request.getRequestEntity(), ex.getMessage(), getStackTrace(ex.getStackTrace())));

        } catch (Exception ex) {
            response = HttpAdapterResponseBuilder.internalServerError();
            logger.error(LogMessageBuilder.build(request.getRequestEntity(), ex.getMessage(), getStackTrace(ex.getStackTrace())));
        }

        return response;
    }

    private List<String> getAuthorizationHeader(RequestDTO<T> request) {
        return request.getRequestEntity().getHeaders().get("Authorization");
    }

    private String parseBasicAuthorizationHeader(List<String> authorizationHeader) {
        String[] parsedAuthorizationHeader = authorizationHeader
                .stream()
                .findFirst()
                .map(value -> value.split("\s"))
                .orElseThrow(SecurityException::new);

        if (!BASIC_AUTHORIZATION_TYPE.equals(parsedAuthorizationHeader[0])) {
            throw new SecurityException("Incorrect authorization type.");
        }

        return parsedAuthorizationHeader[1];
    }

    private String[] extractCredentialsFromAuthenticationToken(String authenticationToken) {
        byte[] decodedAuthenticationToken = Base64.getDecoder().decode(authenticationToken);
        String plainCredentials = new String(decodedAuthenticationToken);
        return plainCredentials.split(":");
    }
}
