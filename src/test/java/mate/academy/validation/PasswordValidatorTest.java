package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private Password constraintAnnotation;
    private UserRegistrationDto userRegistrationDto;
    private String password;
    private String passwordMatch;

    @BeforeEach
    void setUp() {
        password = "StrongPasswordString";
        passwordMatch = "StrongPasswordSpring";
        constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(constraintAnnotation);
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(password);
    }

    @Test
    void isValid_Ok() {
        userRegistrationDto.setRepeatPassword(password);
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto, null));
    }

    @Test
    void isValid_NotOk() {
        userRegistrationDto.setRepeatPassword(passwordMatch);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, null));
        userRegistrationDto.setPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, null));
        userRegistrationDto.setRepeatPassword(null);
        userRegistrationDto.setPassword(password);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, null));
    }
}