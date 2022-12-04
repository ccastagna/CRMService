package com.crmservice.crmservice.domain.usecases.updateuser;

import com.crmservice.crmservice.domain.enums.Role;

import java.util.UUID;

public record UpdateUserResponse(UUID id, String userName, Role role) {}
