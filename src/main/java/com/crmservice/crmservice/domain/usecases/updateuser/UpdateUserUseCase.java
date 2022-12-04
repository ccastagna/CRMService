package com.crmservice.crmservice.domain.usecases.updateuser;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.enums.Role;
import com.crmservice.crmservice.domain.enums.UserState;
import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.services.credentialsvalidators.IUserCredentialsValidator;
import com.crmservice.crmservice.domain.usecases.interfaces.IUpdateUserUseCase;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.crmservice.crmservice.domain.responses.DomainErrorResponse.MALFORMED_PASSWORD;
import static com.crmservice.crmservice.domain.responses.DomainErrorResponse.USER_DOES_NOT_EXIST;

public class UpdateUserUseCase implements IUpdateUserUseCase {

    private final IUserRepositoryService userRepositoryService;
    private final IUserCredentialsValidator userCredentialsValidator;
    private final PasswordEncoder passwordEncoder;

    public UpdateUserUseCase(IUserCredentialsValidator userCredentialsValidator,
                             PasswordEncoder passwordEncoder,
                             IUserRepositoryService userRepositoryService) {
        this.userCredentialsValidator = userCredentialsValidator;
        this.passwordEncoder = passwordEncoder;
        this.userRepositoryService = userRepositoryService;
    }

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest) throws DomainClientException {

        String usernameToUpdate = updateUserRequest.userName();
        String password = updateUserRequest.password();
        Role role = updateUserRequest.role();

        User userToDelete = this.userRepositoryService.getActiveUserByUsername(usernameToUpdate)
                .orElseThrow(new DomainClientException(USER_DOES_NOT_EXIST));

        validatePassword(password);

        String encodedPassword = this.passwordEncoder.encode(password);

        User updatedUser = new User(
                userToDelete.getId(),
                userToDelete.getUsername(),
                encodedPassword,
                role,
                UserState.ACTIVE);

        this.userRepositoryService.saveUser(updatedUser);

        return new UpdateUserResponse(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getRole());
    }

    private void validatePassword(String password) throws DomainClientException {
        if (safeboxPasswordIsUnsecure(password)) {
            throw new DomainClientException(MALFORMED_PASSWORD);
        }
    }
    private boolean safeboxPasswordIsUnsecure(String password) {
        return !userCredentialsValidator.isUserPasswordSecure(password);
    }

}
