package com.crmservice.crmservice.domain.enums;

import java.util.List;

import static com.crmservice.crmservice.domain.enums.UseCase.CREATE_CUSTOMER;
import static com.crmservice.crmservice.domain.enums.UseCase.CREATE_USER;
import static com.crmservice.crmservice.domain.enums.UseCase.DELETE_CUSTOMER;
import static com.crmservice.crmservice.domain.enums.UseCase.DELETE_USER;
import static com.crmservice.crmservice.domain.enums.UseCase.GET_ALL_CUSTOMERS;
import static com.crmservice.crmservice.domain.enums.UseCase.GET_ALL_USERS;
import static com.crmservice.crmservice.domain.enums.UseCase.GET_CUSTOMER;
import static com.crmservice.crmservice.domain.enums.UseCase.UPDATE_CUSTOMER;
import static com.crmservice.crmservice.domain.enums.UseCase.UPDATE_CUSTOMER_PHOTO;
import static com.crmservice.crmservice.domain.enums.UseCase.UPDATE_USER;

public enum Role {
    ROOT(List.of(CREATE_USER)),
    ADMIN(List.of(
            CREATE_USER,
            UPDATE_USER,
            DELETE_USER,
            GET_ALL_USERS)
    ),
    MODERATOR(List.of(
            CREATE_CUSTOMER,
            UPDATE_CUSTOMER,
            UPDATE_CUSTOMER_PHOTO,
            DELETE_CUSTOMER,
            GET_CUSTOMER,
            GET_ALL_CUSTOMERS)
    );

    private List<UseCase> permissions;

    Role(List<UseCase> permissions) {
        this.permissions = permissions;
    }

    public Boolean hasPermissionsTo(UseCase useCase){
        return this.permissions.contains(useCase);
    }
}
