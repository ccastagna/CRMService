package com.crmservice.crmservice.infrastructure.drivers.logs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.RequestEntity;

import java.text.MessageFormat;

public class LogMessageBuilder {

    private static final String LOG_FORMAT_TEMPLATE_WITH_DETAILS =
            "\n\tRequestURL: {0}\n\t RequestHeaders: {1}\n\t RequestBody: {2}\n\t Message: {3}\n\t Details: {4}";
    private static final String LOG_FORMAT_TEMPLATE =
            "\n\tRequestURL: {0}\n\t RequestHeaders: {1}\n\t RequestBody: {2}\n\t Message: {3}";

    private LogMessageBuilder() {
    }

    public static <T> String build(RequestEntity<T> request, String message, String details) {
        String requestUrl = request.getMethod() + " " + request.getUrl();

        return MessageFormat.format(LOG_FORMAT_TEMPLATE_WITH_DETAILS,
                requestUrl, request.getHeaders(), getRequestBody(request), message, details);
    }

    public static <T> String build(RequestEntity<T> request, String message) {
        String requestUrl = request.getMethod() + " " + request.getUrl();

        return MessageFormat.format(LOG_FORMAT_TEMPLATE,
                requestUrl, request.getHeaders(), getRequestBody(request), message);
    }

    private static <T> String getRequestBody(RequestEntity<T> request) {
        String requestBody;
        try {
            requestBody = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(request.getBody());
        } catch (JsonProcessingException ex) {
            requestBody = ex.getMessage();
        }
        return requestBody;
    }
}
