package com.crmservice.crmservice.infrastructure.drivers.responses.dtos;

import com.crmservice.crmservice.domain.usecases.login.LoginResponse;

public record LoginResponseDTO(String token) {

    public static LoginResponseDTO from(LoginResponse loginResponse) {
        return new LoginResponseDTO(loginResponse.token());
    }
}
