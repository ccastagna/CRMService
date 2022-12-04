package com.crmservice.crmservice.domain.usecases.updateuser;

import com.crmservice.crmservice.domain.enums.Role;

public record UpdateUserRequest(String userName, String password, Role role) {}
