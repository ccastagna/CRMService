package com.crmservice.crmservice.domain.interfaces;

import com.crmservice.crmservice.domain.responses.DomainClientException;
import dev.paseto.jpaseto.Paseto;

import java.util.Map;

public interface ITokenService {
    String create(Map<String, Object> claims);

    Paseto validate(String token, Map<String, String> claimsRequired);
}
