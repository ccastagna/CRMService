package com.crmservice.crmservice.domain.usecases.getallusers;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import com.crmservice.crmservice.domain.usecases.interfaces.IGetAllUsersUseCase;

import java.util.List;


public class GetAllUsersUseCase implements IGetAllUsersUseCase {

    private final IUserRepositoryService userRepositoryService;

    public GetAllUsersUseCase(IUserRepositoryService userRepositoryService) {
        this.userRepositoryService = userRepositoryService;
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepositoryService.getAllActiveUsers();
    }

}
