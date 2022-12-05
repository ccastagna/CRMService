package com.crmservice.crmservice.infrastructure.drivers.requests.dtos;

import org.springframework.http.RequestEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestDTO<T> {
    private RequestEntity<T> requestEntity;
    private HttpServletRequest httpServletRequest;
    private Map<RequestContextKey, String> context;
    private MultipartFile photo;

    public RequestDTO(RequestEntity<T> request) {
        this(request, null, new HashMap<>(), null);
    }

    public RequestDTO(HttpServletRequest httpServletRequest, MultipartFile photo) {
        this(null, httpServletRequest, new HashMap<>(), photo);
    }

    public RequestDTO(RequestEntity<T> request, HttpServletRequest httpServletRequest, Map<RequestContextKey, String> context, MultipartFile photo) {
        this.requestEntity = request;
        this.httpServletRequest = httpServletRequest;
        this.context = context;
        this.photo = photo;
    }

    public RequestEntity<T> getRequestEntity() {
        return requestEntity;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public String getContext(RequestContextKey id) {
        return this.context.get(id);
    }

    public void setContext(RequestContextKey key, String value) {
        Optional.of(this.context)
                .map(context -> context.put(key, value));
    }

    public MultipartFile getPhoto() {
        return photo;
    }
}
