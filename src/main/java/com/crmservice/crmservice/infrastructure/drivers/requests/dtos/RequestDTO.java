package com.crmservice.crmservice.infrastructure.drivers.requests.dtos;

import org.springframework.http.RequestEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestDTO<T> {
    private RequestEntity<T> requestEntity;
    private Map<RequestContextKey, String> context;

    public RequestDTO(RequestEntity<T> request) {
        this(request, new HashMap<>());
    }

    public RequestDTO(RequestEntity<T> request, Map<RequestContextKey, String> context) {
        this.requestEntity = request;
        this.context = context;
    }

    public RequestEntity<T> getRequestEntity() {
        return requestEntity;
    }

    public String getContext(RequestContextKey id) {
        return this.context.get(id);
    }

    public void setContext(RequestContextKey key, String value) {
        Optional.of(this.context)
                .map(context -> context.put(key, value));
    }
}
