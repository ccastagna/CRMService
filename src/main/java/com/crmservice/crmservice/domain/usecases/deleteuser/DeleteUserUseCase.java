package com.crmservice.crmservice.domain.usecases.deleteuser;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.enums.UserState;
import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.usecases.interfaces.IDeleteUserUseCase;

import static com.crmservice.crmservice.domain.responses.DomainErrorResponse.USER_DOES_NOT_EXIST;

public class DeleteUserUseCase implements IDeleteUserUseCase {

    private final IUserRepositoryService userRepositoryService;

    public DeleteUserUseCase(IUserRepositoryService userRepositoryService) {
        this.userRepositoryService = userRepositoryService;
    }

    @Override
    public void deleteUser(DeleteUserRequest deleteUserRequest) throws DomainClientException {

        String usernameToDelete = deleteUserRequest.usernameToDelete();

        User userToDelete = this.userRepositoryService.getActiveUserByUsername(usernameToDelete)
                .orElseThrow(new DomainClientException(USER_DOES_NOT_EXIST));

        User deletedUser = new User(
                userToDelete.getId(),
                userToDelete.getUsername(),
                userToDelete.getPassword(),
                userToDelete.getRole(),
                UserState.DELETED);

        this.userRepositoryService.saveUser(deletedUser);
    }
}
