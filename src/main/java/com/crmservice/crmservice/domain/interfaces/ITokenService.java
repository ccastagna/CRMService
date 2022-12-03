package com.crmservice.crmservice.domain.interfaces;

import java.util.Map;

public interface ITokenService {
    String create(Map<String, Object> claims);
}
