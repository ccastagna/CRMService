package com.crmservice.crmservice.domain.usecases.createuser;

import com.crmservice.crmservice.domain.enums.Role;

public record CreateUserRequest(String userName, String password, Role role) {
}
