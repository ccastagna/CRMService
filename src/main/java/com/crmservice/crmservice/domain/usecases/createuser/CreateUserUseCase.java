package com.crmservice.crmservice.domain.usecases.createuser;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.enums.Role;
import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.services.credentialsvalidators.IUserCredentialsValidator;
import com.crmservice.crmservice.infrastructure.drivers.interfaces.ICreateUserUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.crmservice.crmservice.domain.responses.DomainErrorResponse.ALREADY_EXISTENT_USER;
import static com.crmservice.crmservice.domain.responses.DomainErrorResponse.MALFORMED_PASSWORD;
import static com.crmservice.crmservice.domain.responses.DomainErrorResponse.MALFORMED_USERNAME;

public class CreateUserUseCase implements ICreateUserUseCase {

    private final Logger logger = LoggerFactory.getLogger(CreateUserUseCase.class);
    private final IUserCredentialsValidator userCredentialsValidator;
    private final PasswordEncoder passwordEncoder;
    private final IUserRepositoryService userRepositoryService;

    public CreateUserUseCase(IUserCredentialsValidator userCredentialsValidator,
                             PasswordEncoder passwordEncoder,
                             IUserRepositoryService userRepositoryService) {
        this.userCredentialsValidator = userCredentialsValidator;
        this.passwordEncoder = passwordEncoder;
        this.userRepositoryService = userRepositoryService;

        try {
            this.createUser(new CreateUserRequest("root", "@SecUre.P4$5", Role.ROOT));
        }catch (DomainClientException ex) {
            logger.warn(ex.getMessage());
        }
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) throws DomainClientException {

        String userName = createUserRequest.userName();
        String password = createUserRequest.password();

        validateUsername(userName);
        validatePassword(password);

        String encodedPassword = this.passwordEncoder.encode(password);
        User newUser = new User(userName, encodedPassword, createUserRequest.role());

        User createdUser = this.userRepositoryService.createUser(newUser);

        return new CreateUserResponse(createdUser.getId(), createdUser.getUsername(), createdUser.getRole());
    }

    private void validateUsername(String name) throws DomainClientException {
        if (usernameIsInvalid(name)) {
            throw new DomainClientException(MALFORMED_USERNAME);
        }

        if (userCredentialsValidator.isUsernameDuplicated(name)) {
            throw new DomainClientException(ALREADY_EXISTENT_USER);
        }
    }

    private void validatePassword(String password) throws DomainClientException {
        if (safeboxPasswordIsUnsecure(password)) {
            throw new DomainClientException(MALFORMED_PASSWORD);
        }
    }

    private boolean usernameIsInvalid(String name) {
        return !userCredentialsValidator.isUsernameValid(name);
    }

    private boolean safeboxPasswordIsUnsecure(String password) {
        return !userCredentialsValidator.isUserPasswordSecure(password);
    }
}
