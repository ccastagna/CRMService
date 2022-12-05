package com.crmservice.crmservice.domain.services.credentialsvalidators;

import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserCredentialsValidator implements IUserCredentialsValidator {

    private static final String VALID_USERNAME_REGEX = "[a-zA-Z0-9\s]{1,254}";

    private static final String SECURE_PASSWORD_REGEX = "(?=.*[A-Z])" + // At least an uppercase letter
            "(?=.*[a-z])" + // At least a lowercase letter
            "(?=.*[!@#$&.*_-])" + // At least a special character
            "(?=.*[0-9])" + // At least a digit
            ".{12,}"; // At least 12 characters

    private final IUserRepositoryService userRepositoryService;
    private final PasswordEncoder passwordEncoder;

    public UserCredentialsValidator(IUserRepositoryService userRepositoryService,
                                    PasswordEncoder passwordEncoder) {
        this.userRepositoryService = userRepositoryService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isUsernameValid(String name) {
        return Optional.ofNullable(name)
                .map(notNullName -> notNullName.matches(VALID_USERNAME_REGEX))
                .orElse(false);
    }

    @Override
    public boolean isUsernameDuplicated(String name) {
        String notNullName = Optional.of(name).get();
        return this.userRepositoryService.getUserByUsername(notNullName).isPresent();
    }

    @Override
    public boolean isUserPasswordSecure(String password) {
        return Optional.ofNullable(password)
                .filter(notNullPassword -> notNullPassword.matches(SECURE_PASSWORD_REGEX))
                .isPresent();
    }

    @Override
    public boolean isRequestUsernameValid(String name, String requestUsername) {
        return name.equals(requestUsername);
    }

    @Override
    public boolean isRequestUserPasswordValid(String password, String requestPassword) {
        return this.passwordEncoder.matches(requestPassword, password);
    }

}
