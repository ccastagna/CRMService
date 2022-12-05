package com.crmservice.crmservice.domain.usecases.login;

import org.springframework.lang.NonNull;

public class LoginRequest {

    private String requestUsername;
    private String requestPassword;
    private String ip;

    public LoginRequest(@NonNull String requestUsername, @NonNull String requestPassword, @NonNull String ip) {
        this.requestUsername = requestUsername;
        this.requestPassword = requestPassword;
        this.ip = ip;
    }

    public String getRequestUsername() {
        return this.requestUsername;
    }

    public String getRequestPassword() {
        return this.requestPassword;
    }

    public String getIp() {
        return ip;
    }
}



