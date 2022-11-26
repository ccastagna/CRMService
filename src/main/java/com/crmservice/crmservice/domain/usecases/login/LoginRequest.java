package com.crmservice.crmservice.domain.usecases.login;

import com.crmservice.crmservice.domain.responses.DomainClientException;

import java.util.Base64;
import java.util.UUID;

import static com.crmservice.crmservice.domain.responses.DomainErrorResponse.MALFORMED_CREDENTIALS;

public class LoginRequest {
    private String requestUsername;
    private String requestPassword;
    private String ip;

    public LoginRequest(String authenticationToken, String ip) throws DomainClientException {
        extractCredentialsFromAuthenticationToken(authenticationToken);
        this.ip = ip;
    }

    private void extractCredentialsFromAuthenticationToken(String authenticationToken) throws DomainClientException {
        byte[] decodedAuthenticationToken = Base64.getDecoder().decode(authenticationToken);
        String plainCredentials = new String(decodedAuthenticationToken);
        String[] credentials = plainCredentials.split(":");
        if (credentials.length != 2 || credentials[0].isBlank() || credentials[1].isBlank()) {
            throw new DomainClientException(MALFORMED_CREDENTIALS);
        }
        this.requestUsername = credentials[0];
        this.requestPassword = credentials[1];
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
