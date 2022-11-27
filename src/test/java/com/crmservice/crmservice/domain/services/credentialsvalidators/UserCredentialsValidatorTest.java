package com.crmservice.crmservice.domain.services.credentialsvalidators;


import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.enums.Role;
import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static com.crmservice.crmservice.domain.services.credentialsvalidators.CredentialConstants.NAME_WITH_255_NON_SPECIAL_CHARACTERS;
import static com.crmservice.crmservice.domain.services.credentialsvalidators.CredentialConstants.PASSWORD_WITHOUT_LOWERCASE_LETTERS;
import static com.crmservice.crmservice.domain.services.credentialsvalidators.CredentialConstants.PASSWORD_WITHOUT_NUMBERS;
import static com.crmservice.crmservice.domain.services.credentialsvalidators.CredentialConstants.PASSWORD_WITHOUT_SPECIAL_CHARACTERS;
import static com.crmservice.crmservice.domain.services.credentialsvalidators.CredentialConstants.PASSWORD_WITHOUT_UPPERCASE_LETTERS;
import static com.crmservice.crmservice.domain.services.credentialsvalidators.CredentialConstants.PASSWORD_WITH_11_CHARACTERS;
import static com.crmservice.crmservice.domain.services.credentialsvalidators.CredentialConstants.STRONG_PASSWORD;
import static com.crmservice.crmservice.domain.services.credentialsvalidators.CredentialConstants.USERNAME_WITH_SPECIAL_CHARACTERS;
import static com.crmservice.crmservice.domain.services.credentialsvalidators.CredentialConstants.USERNAME_WITH_254_NON_SPECIAL_CHARACTERS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserCredentialsValidatorTest {

    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final UUID USER_ID = UUID.randomUUID();

    private IUserCredentialsValidator userCredentialsValidator;
    @Mock
    private IUserRepositoryService userRepositoryService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.userCredentialsValidator = new UserCredentialsValidator(userRepositoryService, passwordEncoder);
    }

    @Test
    void givenNotEmptyNameWith254NonSpecialCharacters_whenIsUsernameValid_thenReturnTrue() {
        //given
        String validUsername = USERNAME_WITH_254_NON_SPECIAL_CHARACTERS;

        //when
        boolean validationResult = this.userCredentialsValidator.isUsernameValid(validUsername);

        //then
        assertTrue(validationResult);
    }

    @Test
    void givenNullUsername_whenIsUsernameValid_thenReturnFalse() {
        //when
        boolean validationResult = this.userCredentialsValidator.isUsernameValid(null);

        //then
        assertFalse(validationResult);
    }

    @Test
    void givenEmptyUsername_whenIsUsernameValid_thenReturnFalse() {
        //given
        String invalidUsername = "";

        //when
        boolean validationResult = this.userCredentialsValidator.isUsernameValid(invalidUsername);

        //then
        assertFalse(validationResult);
    }

    @Test
    void givenUsernameWithSpecialCharacters_whenIsUsernameValid_thenReturnFalse() {
        //given
        String invalidUsername = USERNAME_WITH_SPECIAL_CHARACTERS;

        //when
        boolean validationResult = this.userCredentialsValidator.isUsernameValid(invalidUsername);

        //then
        assertFalse(validationResult);
    }

    @Test
    void givenUsernameWith255Characters_whenIsUsernameValid_thenReturnFalse() {
        //given
        String invalidUsername = NAME_WITH_255_NON_SPECIAL_CHARACTERS;

        //when
        boolean validationResult = this.userCredentialsValidator.isUsernameValid(invalidUsername);

        //then
        assertFalse(validationResult);
    }

    @Test
    void givenExistentUsername_whenIsUsernameDuplicated_thenReturnTrue() {
        //given
        String validUsername = USERNAME_WITH_254_NON_SPECIAL_CHARACTERS;
        when(userRepositoryService.getUserByUsername(validUsername))
                .thenReturn(Optional.of(new User(USER_ID, validUsername, ENCODED_PASSWORD, Role.ROOT)));

        //when
        boolean validationResult = this.userCredentialsValidator.isUsernameDuplicated(validUsername);

        //then
        assertTrue(validationResult);
    }

    @Test
    void givenNonExistentUsername_whenIsUsernameDuplicated_thenReturnFalse() {
        //given
        String validUsername = USERNAME_WITH_254_NON_SPECIAL_CHARACTERS;
        when(userRepositoryService.getUserByUsername(validUsername)).thenReturn(Optional.empty());

        //when
        boolean validationResult = this.userCredentialsValidator.isUsernameDuplicated(validUsername);

        //then
        assertFalse(validationResult);
    }

    /*
     *   A strong password must have
     *   - at least 12 characters
     *   - at least an uppercase letter
     *   - at least a lowercase letter
     *   - at least one special character
     *   - at least one number
     */
    @Test
    void givenStrongPassword_whenIsUserPasswordSecure_thenReturnTrue() {
        //given
        String userSecurePassword = STRONG_PASSWORD;

        //when
        boolean validationResult = this.userCredentialsValidator.isUserPasswordSecure(userSecurePassword);

        //then
        assertTrue(validationResult);
    }

    @Test
    void givenNullPassword_whenIsUserPasswordSecure_thenReturnFalse() {
        //when
        boolean validationResult = this.userCredentialsValidator.isUserPasswordSecure(null);

        //then
        assertFalse(validationResult);
    }

    @Test
    void givenEmptyPassword_whenIsUserPasswordSecure_thenReturnFalse() {
        //given
        String userUnsecurePassword = "";

        //when
        boolean validationResult = this.userCredentialsValidator.isUserPasswordSecure(userUnsecurePassword);

        //then
        assertFalse(validationResult);
    }

    @Test
    void givenPasswordWith11Characters_whenIsUserPasswordSecure_thenReturnFalse() {
        //given
        String userUnsecurePassword = PASSWORD_WITH_11_CHARACTERS;

        //when
        boolean validationResult = this.userCredentialsValidator.isUserPasswordSecure(userUnsecurePassword);

        //then
        assertFalse(validationResult);
    }

    @Test
    void givenPasswordWithoutUppercaseLetters_whenIsUserPasswordSecure_thenReturnFalse() {
        //given
        String userUnsecurePassword = PASSWORD_WITHOUT_UPPERCASE_LETTERS;

        //when
        boolean validationResult = this.userCredentialsValidator.isUserPasswordSecure(userUnsecurePassword);

        //then
        assertFalse(validationResult);
    }

    @Test
    void givenPasswordWithoutLowercaseLetters_whenIsUserPasswordSecure_thenReturnFalse() {
        //given
        String userUnsecurePassword = PASSWORD_WITHOUT_LOWERCASE_LETTERS;

        //when
        boolean validationResult = this.userCredentialsValidator.isUserPasswordSecure(userUnsecurePassword);

        //then
        assertFalse(validationResult);
    }

    @Test
    void givenPasswordWithoutSpecialCharacters_whenIsUserPasswordSecure_thenReturnFalse() {
        //given
        String userUnsecurePassword = PASSWORD_WITHOUT_SPECIAL_CHARACTERS;

        //when
        boolean validationResult = this.userCredentialsValidator.isUserPasswordSecure(userUnsecurePassword);

        //then
        assertFalse(validationResult);
    }

    @Test
    void givenPasswordWithoutNumbers_whenIsUserPasswordSecure_thenReturnFalse() {
        //given
        String userUnsecurePassword = PASSWORD_WITHOUT_NUMBERS;

        //when
        boolean validationResult = this.userCredentialsValidator.isUserPasswordSecure(userUnsecurePassword);

        //then
        assertFalse(validationResult);
    }
}
