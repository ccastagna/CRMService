package com.crmservice.crmservice.infrastructure.drivers.requesthandlers;

import com.crmservice.crmservice.infrastructure.drivers.logs.LogMessageBuilder;
import com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestDTO;
import com.crmservice.crmservice.infrastructure.drivers.responses.HttpAdapterResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.crmservice.crmservice.infrastructure.drivers.requests.dtos.RequestContextKey.REQUEST_IP;

public class IPHeaderHandler<T, R> extends BaseRequestHandler<T, R> {

    private final Logger logger = LoggerFactory.getLogger(IPHeaderHandler.class);
    private static final String HEADER_NAME = "X-Forwarded-For";
    private static final String IPV4_REGEX =
            "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    private static final Pattern IPv4_PATTERN = Pattern.compile(IPV4_REGEX);

    @Override
    public ResponseEntity<R> handle(RequestDTO<T> request) {

        ResponseEntity response;

        try {
            String ip = extractClientIP(getXForwardedForHeader(request));

            if (!isValidIPv4(ip)) {
                String message = "Request client IP is invalid";
                logger.error(LogMessageBuilder.build(request.getRequestEntity(), message));
                return HttpAdapterResponseBuilder.badRequest(message);
            }

            request.setContext(REQUEST_IP, ip);

            response = super.handle(request);

        } catch (NoSuchElementException ex) {
            String message = "Request client IP is missing";
            response = HttpAdapterResponseBuilder.badRequest(message);
            logger.error(LogMessageBuilder.build(request.getRequestEntity(), ex.getMessage(), message));

        } catch (Exception ex) {
            response = HttpAdapterResponseBuilder.internalServerError();
            logger.error(LogMessageBuilder.build(request.getRequestEntity(), ex.getMessage(), getStackTrace(ex.getStackTrace())));
        }

        return response;
    }

    private List<String> getXForwardedForHeader(RequestDTO<T> request) {
        return Optional.ofNullable(request.getRequestEntity())
                .map(requestEntity -> requestEntity.getHeaders().get(HEADER_NAME))
                .orElseGet(() -> List.of(
                        request.getHttpServletRequest().getHeader(HEADER_NAME)
                ));
    }

    private String extractClientIP(List<String> xForwardedForHeader) {
        String[] parsedXForwardedForHeader = xForwardedForHeader
                .stream()
                .findFirst()
                .map(value -> value.split(","))
                .orElseThrow();

        return parsedXForwardedForHeader[0].trim();
    }

    private static boolean isValidIPv4(String ip) {
        return Optional.ofNullable(ip)
                .map(IPv4_PATTERN::matcher)
                .map(Matcher::matches)
                .orElse(false);
    }

}
