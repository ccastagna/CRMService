package com.crmservice.crmservice.infrastructure.drivers.logs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.RequestEntity;

import java.text.MessageFormat;
import java.util.Optional;

public class LogMessageBuilder {

    private static final String LOG_FORMAT_TEMPLATE_WITH_DETAILS =
            "\n\tRequestURL: {0}\n\t RequestHeaders: {1}\n\t RequestBody: {2}\n\t Message: {3}\n\t Details: {4}";
    private static final String LOG_FORMAT_TEMPLATE =
            "\n\tRequestURL: {0}\n\t RequestHeaders: {1}\n\t RequestBody: {2}\n\t Message: {3}";
    private static final String DEFAULT_LOG_FORMAT_TEMPLATE = "\n\tMessage: {0}\n\t Details: {1}";

    private LogMessageBuilder() {
    }

    public static <T> String build(RequestEntity<T> request, String message, String details) {

        return Optional.ofNullable(request)
                .map(req -> {
                    String requestUrl = request.getMethod() + " " + request.getUrl();
                    return MessageFormat.format(LOG_FORMAT_TEMPLATE_WITH_DETAILS,
                            requestUrl, request.getHeaders(), getRequestBody(request), message, details);
                })
                .orElseGet(() -> {
                    return MessageFormat.format(DEFAULT_LOG_FORMAT_TEMPLATE, message, details);
                });
    }

    public static <T> String build(RequestEntity<T> request, String message) {
        return Optional.ofNullable(request)
                .map(req -> {
                    String requestUrl = request.getMethod() + " " + request.getUrl();
                    return MessageFormat.format(LOG_FORMAT_TEMPLATE,
                            requestUrl, request.getHeaders(), getRequestBody(request), message);
                })
                .orElse(message);
    }

    private static <T> String getRequestBody(RequestEntity<T> request) {

        return Optional.ofNullable(request)
                .map(req -> {
                    try {
                        return new ObjectMapper()
                                .writerWithDefaultPrettyPrinter()
                                .writeValueAsString(req.getBody());
                    } catch (JsonProcessingException ex) {
                        return ex.getMessage();
                    }
                })
                .orElse("");


    }
}
