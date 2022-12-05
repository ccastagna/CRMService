package com.crmservice.crmservice.domain.usecases.createuser;

import com.crmservice.crmservice.domain.enums.Role;

import java.util.UUID;

public record CreateUserResponse(UUID id, String userName, Role role) {
}
