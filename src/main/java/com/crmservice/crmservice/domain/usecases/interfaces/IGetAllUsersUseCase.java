package com.crmservice.crmservice.domain.usecases.interfaces;

import com.crmservice.crmservice.domain.entities.User;

import java.util.List;

public interface IGetAllUsersUseCase {
    List<User> getAllUsers();
}
